package at.htl.control;

import at.htl.entity.Exam;
import at.htl.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    @Inject
    ExamRepository examRepository;
    public User findByName(String lastName, String firstName) {
        var query = this.getEntityManager().createQuery(
                        "select e from User e where e.firstName = :firstName and e.lastName = :lastName",
                        User.class
                )
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName);
        return query.getSingleResult();
    }

    public User findByEmail(String email) {
        var query = this.getEntityManager().createQuery(
                        "select e from User e where e.email = :email",
                        User.class
                )
                .setParameter("email", email);
        return query.getSingleResult();
    }

    public boolean checkIfUserExamineeIsStillOnline(User userexaminee, int interval) {
        if(userexaminee.lastOnline == null) {
            return false;
        }
        long seconds = Duration.between(userexaminee.lastOnline, LocalDateTime.now()).toSeconds();
        if(seconds > (interval* 3L)) {
            return false;
        }
        return true;
    }

    public List<User> listAllOrdered() {
        return this.listAll(Sort.by("lastName").and("firstName"));
    }


}

