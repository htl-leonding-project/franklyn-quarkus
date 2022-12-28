package at.htl.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import at.htl.control.ExaminerRepository;
import at.htl.control.SchoolClassRepository;
import at.htl.entity.Examiner;
import at.htl.entity.SchoolClass;
import io.quarkus.logging.Log;
import org.bytedream.untis4j.Session;
import org.bytedream.untis4j.responseObjects.Classes;
import org.bytedream.untis4j.responseObjects.SchoolYears;
import org.bytedream.untis4j.responseObjects.Teachers;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.net.SocketException;

@ApplicationScoped
public class WebUntisService {

    @ConfigProperty(name = "auth.school.name")
    String schoolName;

    @ConfigProperty(name = "auth.untis.server")
    String serverName;

    @Inject
    ExaminerRepository examinerRepository;

    @Inject
    SchoolClassRepository schoolClassRepository;

    Session session;

    public String authenticateUser(String userName, String password) {
        try {
            Session session = Session.login(
                    userName,
                    password,
                    this.serverName,
                    this.schoolName.replace(" ", "%20"));

            if (session != null) {

                session.logout();
                Log.info("success");
                return "success";
            } else {
                Log.info("failed");
                return "failed";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Unknown error";
    }

    public String initDB(String userName, String password) {
        Log.info("Initializing DB...");
        try {
                session = Session.login(
                    userName,
                    password,
                    this.serverName,
                    this.schoolName.replace(" ", "%20"));


            Log.info("Session: " + session);
            if (session != null) {
                var teachers = session.getTeachers();
                var schoolClasses = session.getClasses();
                var currentSchoolYear = session.getCurrentSchoolYear();
                persistExaminers(teachers);
                persistSchoolClasses(schoolClasses, currentSchoolYear);

                Log.info("WebUntis DB initialized");
                session.logout();
                return teachers.stream().findFirst().get().toString();
            } else {
                Log.info("Login failed");
                return "Login failed";
            }
        } catch (IOException e) {
            Log.info(e.getMessage());
            e.printStackTrace();
        }

        return "Unknown error";
    }

    @Transactional
    public void persistExaminers(Teachers result){
        String name= "";

        for (var teacher : result)  {
            Examiner examiner = new Examiner();
            name = teacher.getFullName();
            if(name.contains(" ")){
                examiner.isAdmin = false;
                examiner.firstName = name.split(" ")[0];
                examiner.lastName = name.split(" ")[1];
                examiner.userName = teacher.getName();
                examinerRepository.persist(examiner);
            }
            Log.info(name);
        }
    }

    @Transactional
    public void persistSchoolClasses(Classes schoolClasses, SchoolYears.SchoolYearObject currentSchoolYear){
        String name= "";

        for (var form : schoolClasses)  {
            SchoolClass schoolClass = new SchoolClass();
            schoolClass.title = form.getName();
            schoolClass.year = currentSchoolYear.getName();
            schoolClassRepository.persist(schoolClass);
        }
    }

    public String tryOut(String userName, String password) {
        Log.info("Initializing DB...");
        try {
            session = Session.login(
                    userName,
                    password,
                    this.serverName,
                    this.schoolName.replace(" ", "%20"));


            Log.info("Session: " + session);
            if (session != null) {

                Log.info("WebUntis DB initialized");
                session.logout();
            } else {
                Log.info("Login failed");
                return "Login failed";
            }
        } catch (IOException e) {
            Log.info(e.getMessage());
            e.printStackTrace();
        }

        return "Unknown error";
    }
}
