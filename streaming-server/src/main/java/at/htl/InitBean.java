package at.htl;

import at.htl.control.ExamService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.time.LocalDate;

@ApplicationScoped
public class InitBean {

    @Inject
    ExamService examService;

    public void init(@Observes StartupEvent ev) {
        System.out.println(examService.initializeExam("ITP Test", LocalDate.now()));
    }
}
