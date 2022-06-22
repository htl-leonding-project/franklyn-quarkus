package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.*;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.Session;
import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.reactive.stage.Stage;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.quarkus.hibernate.reactive.panache.PanacheEntity_.id;

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


    @ReactiveTransactional
    public void init(@Observes StartupEvent event){
        List<Examiner> examiners = new ArrayList<>();
        Examiner examiner1 = new Examiner("tmelch", "Tamara", "Melcher", true);
        Examiner examiner2 = new Examiner("mtran", "Michael", "Tran", true);
        Examiner examiner3 = new Examiner("tstuet", "Thomas", "Stuetz", true);
        examiners.add(examiner1);
        examiners.add(examiner2);
        examiners.add(examiner3);
        for (Examiner examiner : examiners) {
            examinerRepository.persist(examiner).subscribe().with(e -> Log.info(examiner.lastName));
        }

        List<SchoolClass> schoolClasses = new ArrayList<>();
        SchoolClass schoolClass1 = new SchoolClass("Franklyn", "2022");
        SchoolClass schoolClass2 = new SchoolClass("3AHIF", "2021");
        schoolClasses.add(schoolClass1);
        schoolClasses.add(schoolClass2);
        for (SchoolClass schoolClass : schoolClasses) {
            schoolClassRepository.persist(
                    schoolClass
            ).subscribe().with(sc -> Log.info(sc.title));
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
            examineeRepository.persist(examinee).subscribe().with(e -> Log.info(examinee.lastName));
        }

        Exam exam1 = new Exam(
                "234",
                "Test",
                true,
                schoolClasses,
                LocalDate.parse("2022-06-23"),
                LocalDateTime.parse("2022-05-23T17:09:42.411"),
                LocalDateTime.parse("2022-05-23T18:09:42.411"),
                examinees,
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
                examinees,
                examiners,
                5,
                Resolution.HD,
                1
        );
        examRepository.persist(exam1).subscribe().with(e -> Log.info(exam1.title));
        examRepository.persist(exam1).subscribe().with(e -> Log.info(exam2.title));

        Screenshot screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                exam1,
                examinee1,
                Resolution.HD,
                1
        );
        screenshotRepository.persist(screenshot1).subscribe().with(s -> {Log.info("Screenshot saved");});

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                2L,
                exam1,
                examinee1,
                Resolution.HD,
                1
        );
        screenshotRepository.persist(screenshot1).subscribe().with(s -> {Log.info("Screenshot saved");});


        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                1L,
                exam2,
                examinee2,
                Resolution.HD,
                1
        );
        screenshotRepository.persist(screenshot1).subscribe().with(s -> {Log.info("Screenshot saved");});

        screenshot1 = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                2L,
                exam2,
                examinee2,
                Resolution.HD,
                1
        );
        screenshotRepository.persist(screenshot1).subscribe().with(s -> {Log.info("Screenshot saved");});

    }
}
