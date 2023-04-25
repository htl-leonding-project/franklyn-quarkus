package at.htl.control;

import at.htl.entity.*;
import at.htl.service.WebUntisService;
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

    @Inject
    WebUntisService webUntisService;


    @Transactional
    public void init(@Observes StartupEvent event){
        /*List<Examiner> examiners = new ArrayList<>();
        Examiner examiner1 = new Examiner("tmelch", "Tamara", "Melcher", true);
        Examiner examiner2 = new Examiner("mtran", "Michael", "Tran", true);
        Examiner examiner3 = new Examiner("tstuet", "Thomas", "Stuetz", true);
        examiners.add(examiner1);
        examiners.add(examiner2);
        examiners.add(examiner3);*/



        String webUntisResult = webUntisService.initDB("NNRADIO", "PAuthlyn28");
        Log.info("WebUntis Result: " + webUntisResult);
        Examiner examinerWU = examinerRepository.findByUsername("NNRADIO");
        //examiners.add(examinerWU);

        /*List<SchoolClass> schoolClasses = new ArrayList<>();
        SchoolClass schoolClass1 = new SchoolClass("5AHIF", "2022");
        SchoolClass schoolClass2 = new SchoolClass("3AHIF", "2021");
        schoolClasses.add(schoolClass1);
        schoolClasses.add(schoolClass2);
        for (SchoolClass schoolClass : schoolClasses) {
            schoolClassRepository.persist(schoolClass);
            Log.info("Persist Schoolclass: " + schoolClass.title);
        }
        List<SchoolClass> schoolClasses2 = new ArrayList<>();
        schoolClasses2.add(schoolClass2);


        Exam exam1 = new Exam(
                "234",
                "Test",
                ExamState.FINISHED,
                schoolClasses2,
                LocalDate.parse("2022-06-23"),
                LocalDateTime.parse("2022-05-23T17:09:42.411"),
                LocalDateTime.parse("2022-05-23T18:09:42.411"),
                5,
                examiners
                );
        List<Examiner> examiners1 = new ArrayList<>();
        examiners1.add(examinerWU);
        examiners1.add(examiner2);
        examiners1.add(examiner3);
        exam1.examiners = examiners1;
        exam1.adminId = examinerWU.id;

        Exam exam2 = new Exam(
                "948",
                "Franklyn",
                ExamState.RUNNING,
                schoolClasses,
                LocalDate.parse("2022-02-23"),
                LocalDateTime.parse("2022-02-23T10:09:42.411"),
                LocalDateTime.parse("2022-02-23T12:09:42.411"),
                5,
                examiners
        );
        exam1.schoolClasses = schoolClasses2;
        exam2.schoolClasses = schoolClasses;
        examRepository.persist(exam1);
        examRepository.persist(exam2);
        List<Examiner> examiners2 = new ArrayList<>();
        examiners2.add(examinerWU);
        examiners2.add(examiner2);
        exam2.examiners = examiners2;
        exam2.adminId = examinerWU.id;
        Log.info("Persist: " + exam1.title);
        Log.info("Persist: " + exam2.title);

        examiner1.exams = List.of(exam1, exam2);
        examiner2.exams = List.of(exam1);
        examiner3.exams = List.of(exam1, exam2);

        List<Examinee> examinees = new ArrayList<>();
        Examinee examinee1 = new Examinee("Jakob", "Unterberger");
        examinee1.exam = exam1;
        examinee1.isOnline = true;
        Examinee examinee2 = new Examinee("Jan", "Melcher");
        examinee2.lastOnline = LocalDateTime.now();
        examinee2.exam = exam1;
        examinee1.isOnline = false;
        Examinee examinee3 = new Examinee("Anna", "Wiesinger");
        examinee3.lastOnline = LocalDateTime.now();
        examinee3.exam = exam2;
        examinee1.isOnline = true;
        Examinee examinee4 = new Examinee("Alina", "Schuster");
        examinee4.exam = exam2;
        examinee1.isOnline = false;
        examinees.add(examinee1);
        examinees.add(examinee2);
        examinees.add(examinee3);
        examinees.add(examinee4);
        for (Examinee examinee : examinees) {
            examineeRepository.persist(examinee);
            Log.info("Saved Examinee: " + examinee.lastName);
        }

        Screenshot screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                examinee1,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                2L,
                examinee1,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                examinee2,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                2L,
                examinee2,
                "here"
        );
        screenshotRepository.persist(screenshot1);
        Log.info("Saved Screenshot");*/
    }
}
