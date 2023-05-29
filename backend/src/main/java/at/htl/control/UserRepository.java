package at.htl.control;

import at.htl.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class UserRepository implements PanacheRepository<User> {
    public User findByName(Long examId, String lastName, String firstName) {
        var query = this.getEntityManager().createQuery(
                        "select e from User e where e.exam.id = :id and e.firstName = :firstName and e.lastName = :lastName",
                        User.class
                ).setParameter("id", examId)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName);
        return query.getSingleResult();
    }
}
