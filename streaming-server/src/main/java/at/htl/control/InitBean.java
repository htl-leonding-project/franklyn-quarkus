package at.htl.control;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import nu.pattern.OpenCV;

import java.time.LocalDate;

public class InitBean {

    @Inject
    ExamService examService;

    void onStart(@Observes StartupEvent ev) {

        OpenCV.loadLocally();

        System.out.println(examService.initializeExam("ITP Test", LocalDate.now()));

    }

}
