package at.htl.control;

import at.htl.entity.Exam;
import at.htl.entity.User;
import at.htl.entity.UserRole;
import at.htl.entity.UserSession;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class UserSessionRepository implements PanacheRepository<UserSession> {

    @Inject
    UserSessionRepository userSessionRepository;

    @Inject
    ExamRepository examRepository;

    public UserSession findByName(Long id, String s, String s1) {
        var query = this.getEntityManager().createQuery(
                "select e from UserSession e where e.id = :id and e.user.firstName = :s and e.user.firstName = :s1"
        ).setParameter("id", id)
                .setParameter("s", s)
                .setParameter("s1", s1);
        return (UserSession) query.getSingleResult();
    }

    public List<User> getUserByRole(UserRole role) {
        var query = this.getEntityManager().createQuery(
                "select e from UserSession e where e.userRole = :role"
        ).setParameter("role", role);
        return query.getResultList();
    }

    public User getUserByRoleAndId(UserRole role, Long id) {
        var query = this.getEntityManager().createQuery(
                "select e from UserSession e where e.userRole = :role and e.id = :id"
        ).setParameter("role", role)
                .setParameter("id", id);
        return (User) query.getSingleResult();
    }

    public boolean checkIfUserIsStillOnline(User user, int interval) {
        if(user.lastOnline == null) {
            return false;
        }
        long seconds = Duration.between(user.lastOnline, LocalDateTime.now()).toSeconds();
        if(seconds > (interval* 3L)) {
            return false;
        }
        return true;
    }

    public List<UserSession> listAllOrdered() {
        return this.listAll(Sort.by("lastName").and("firstName"));
    }

    public boolean checkIfAlreadyEnrolled(String firstName, String lastName, Long id) {
        var query = this.getEntityManager().createQuery(
                        "select e from UserSession e where e.exam.id = :id and e.user.firstName = :firstName and e.user.lastName = :lastName",
                        User.class
                ).setParameter("id", id)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName);
        List<User> users = query.getResultList();
        return users.size() > 0;
    }

    public int getCountOfExamineesByExamId(Long id) {
        var query = this.getEntityManager().createQuery(
                "select e from UserSession e where e.exam.id = :id",
                UserSession.class
        ).setParameter("id", id);
        List<UserSession> users = query.getResultList();
        return users.size();
    }

    public void deleteExamFromExaminers(Long examId) {
        var query = this.getEntityManager().createQuery(
                "select e from UserSession e where e.exam.id = :id",
                UserSession.class
        ).setParameter("id", examId);
        Log.info("Test");

        List<UserSession> examinees = query.getResultList();
        for (UserSession ex : examinees) {
            this.getEntityManager().remove(ex);
        }
    }

    public List<User> getAllByExam(String examId) {
        var query = this.getEntityManager().createQuery(
                "select e from UserSession e where e.exam.id = :id",
                User.class
        ).setParameter("id", examId);
        return query.getResultList();
    }
}
