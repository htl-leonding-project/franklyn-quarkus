package at.htl.control;

import at.htl.entity.*;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InitBean {

    @Inject
    UserRepository userRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    UserSessionRepository userSessionRepository;

    @Transactional
    public void init(@Observes StartupEvent ev){
        User testUser01 = new User("Max", "Muster", false);
        User testUser02 = new User("Susi", "Sonne", false);

        List<User> testUsers = List.of(testUser01,testUser02);
        userRepository.persist(testUsers);
        Exam testExam = new Exam("123",
                "test-exam",
                ExamState.IN_PREPARATION,
                LocalDate.now(),
                LocalDateTime.of(2023,12,23,8,0),
                LocalDateTime.of(2023,12,23,10,0),
                5);
        examRepository.persist(testExam);

        testUsers.forEach(user -> {
            UserSession userSession = new UserSession(user, testExam, UserRole.EXAMINEE);
            userSessionRepository.persist(userSession);
        });


    }
}
