package at.htl.control;

import at.htl.boundary.StreamingServerService;
import at.htl.entity.*;
import at.htl.entity.dto.ExamDto;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class InitBean {

    @Inject
    UserRepository userRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    @RestClient
    StreamingServerService streamingServerService;

    @Inject
    UserSessionRepository userSessionRepository;

    @Transactional
    public void init(@Observes StartupEvent ev) {
        User testUser01 = new User("Max", "Muster", false);
        User testUser02 = new User("Susi", "Sonne", false);

        List<User> testUsers = List.of(testUser01, testUser02);
        userRepository.persist(testUsers);
        Exam testExam = new Exam("123",
                "init-bean-test-exam",
                ExamState.IN_PREPARATION,
                LocalDate.now(),
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(4),
                5);
        ExamDto examDto = new ExamDto("init-bean-test-exam",
                ExamState.IN_PREPARATION,
                LocalDate.now(),
                LocalDateTime.now().plusHours(2),
                LocalDateTime.now().plusHours(4),
                5);
        examRepository.persist(testExam);
        streamingServerService.createExam(examDto);

        testUsers.forEach(user -> {
            UserSession userSession = new UserSession(user, testExam, UserRole.EXAMINEE);
            userSessionRepository.persist(userSession);
        });


    }
}
