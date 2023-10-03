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
import java.util.List;
import java.util.Objects;

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

    public boolean authenticateUser(String userName, String password) {
        try {
            Session session = Session.login(
                    userName,
                    password,
                    this.serverName,
                    this.schoolName.replace(" ", "%20"));

            if (session != null) {

                session.logout();
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
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
        List<Examiner> examiners = examinerRepository.listAll();
        boolean alreadyExists = false;
        for (var teacher : result)  {
            Examiner examiner = new Examiner();
            name = teacher.getFullName();
            for (Examiner ex : examiners) {
                if(Objects.equals(ex.firstName, name.split(" ")[0]) && Objects.equals(ex.lastName, name.split(" ")[1]))
                    alreadyExists = true;
            }
            if(!alreadyExists){
                if(name.contains(" ")){
                    examiner.isAdmin = false;
                    examiner.firstName = name.split(" ")[0];
                    examiner.lastName = name.split(" ")[1];
                    examiner.userName = teacher.getName();
                    examinerRepository.persist(examiner);
                }
            }
            alreadyExists = false;
            Log.info(name);
        }
    }

    @Transactional
    public void persistSchoolClasses(Classes schoolClasses, SchoolYears.SchoolYearObject currentSchoolYear){
        String name= "";
        boolean alreadyExists = false;
        List<SchoolClass> scs = schoolClassRepository.listAll();
        for (var form : schoolClasses)  {
            for (SchoolClass sc : scs) {
                if(Objects.equals(sc.title, form.getName()) && Objects.equals(sc.year, currentSchoolYear.getName()))
                    alreadyExists = true;
            }
            if(!alreadyExists){
                SchoolClass schoolClass = new SchoolClass();
                schoolClass.title = form.getName();
                schoolClass.year = currentSchoolYear.getName();
                schoolClassRepository.persist(schoolClass);
            }
            alreadyExists = false;
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
