package at.htl.control;

import at.htl.entity.*;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InitBean {

    @Inject
    ExamineeRepository examineeRepository;

    @Inject
    ExaminerRepository examinerRepository;

    @Inject
    SchoolClassRepository schoolClassRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    ScreenshotRepository screenshotRepository;


    @Transactional
    public void init(@Observes StartupEvent event){
        List<Examiner> examiners = new ArrayList<>();
        Examiner examiner1 = new Examiner("tmelch", "Tamara", "Melcher", true);
        Examiner examiner2 = new Examiner("mtran", "Michael", "Tran", true);
        Examiner examiner3 = new Examiner("tstuet", "Thomas", "Stuetz", true);
        examiners.add(examiner1);
        examiners.add(examiner2);
        examiners.add(examiner3);
        for (Examiner examiner : examiners) {
            examinerRepository.persist(examiner);
            Log.info("Saved Eximiner: " + examiner.lastName);
        }

        List<SchoolClass> schoolClasses = new ArrayList<>();
        SchoolClass schoolClass1 = new SchoolClass("Franklyn", "2022");
        SchoolClass schoolClass2 = new SchoolClass("3AHIF", "2021");
        schoolClasses.add(schoolClass1);
        schoolClasses.add(schoolClass2);
        for (SchoolClass schoolClass : schoolClasses) {
            schoolClassRepository.persist(schoolClass);
            Log.info("Persist Schoolclass: " + schoolClass.title);
        }

        List<Examinee> examinees = new ArrayList<>();
        Examinee examinee1 = new Examinee("Jakob", "Unterberger");
        Examinee examinee2 = new Examinee("Jan", "Melcher");
        Examinee examinee3 = new Examinee("Anna", "Wiesinger");
        Examinee examinee4 = new Examinee("Alina", "Schuster");
        examinees.add(examinee1);
        examinees.add(examinee2);
        examinees.add(examinee3);
        examinees.add(examinee4);
        for (Examinee examinee : examinees) {
            examineeRepository.persist(examinee);
        Log.info("Saved Examinee: " + examinee.lastName);
        }

        Exam exam1 = new Exam(
                "234",
                "Test",
                true,
                schoolClasses,
                LocalDate.parse("2022-06-23"),
                LocalDateTime.parse("2022-05-23T17:09:42.411"),
                LocalDateTime.parse("2022-05-23T18:09:42.411"),
                examiners,
                5,
                Resolution.HD,
                1
                );

        Exam exam2 = new Exam(
                "948",
                "Franklyn",
                false,
                schoolClasses,
                LocalDate.parse("2022-02-23"),
                LocalDateTime.parse("2022-02-23T10:09:42.411"),
                LocalDateTime.parse("2022-02-23T12:09:42.411"),
                examiners,
                5,
                Resolution.HD,
                1
        );
        examRepository.persist(exam1);
        examRepository.persist(exam2);
        Log.info("Persist: " + exam1.title);
        Log.info("Persist: " + exam2.title);

        Screenshot screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                examinee1,
                Resolution.HD,
                1,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                2L,
                examinee1,
                Resolution.HD,
                1,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                examinee2,
                Resolution.HD,
                1,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                2L,
                examinee2,
                Resolution.HD,
                1,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");
    }
}
