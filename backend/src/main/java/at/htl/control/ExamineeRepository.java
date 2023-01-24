package at.htl.control;

import at.htl.entity.Examinee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;


@ApplicationScoped
public class ExamineeRepository implements PanacheRepository<Examinee> {

    @Inject
    ExamRepository examRepository;

    public int getCountOfExamineesByExamId(Long id) {
        var query = this.getEntityManager().createQuery(
                "select e from Examinee e where e.exam.id = :id",
                Examinee.class
        ).setParameter("id", id);
        List<Examinee> examinees = query.getResultList();
        return examinees.size();
    }

    public boolean checkIfAlreadyEnrolled(String firstName, String lastName, Long id) {
        var query = this.getEntityManager().createQuery(
                "select e from Examinee e where e.exam.id = :id and e.firstName = :firstName and e.lastName = :lastName",
                Examinee.class
        ).setParameter("id", id)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName);
        List<Examinee> examinees = query.getResultList();
        return examinees.size() > 0;
    }

    public Examinee findByName(Long examId, String lastName, String firstName) {
        var query = this.getEntityManager().createQuery(
                        "select e from Examinee e where e.exam.id = :id and e.firstName = :firstName and e.lastName = :lastName",
                        Examinee.class
                ).setParameter("id", examId)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName);
        return query.getSingleResult();
    }

    public void deleteExamFromExaminees(Long id) {
        var query = this.getEntityManager().createQuery(
                "select e from Examinee e where e.exam.id = :id",
                Examinee.class
        ).setParameter("id", id);
        Log.info("Test");

        List<Examinee> examinees = query.getResultList();
        for (Examinee ex : examinees) {
            this.getEntityManager().remove(ex);
        }
    }

    public boolean checkIfExamineeIsStillOnline(Examinee examinee, int interval) {
        if(examinee.lastOnline == null) {
            return false;
        }
        long seconds = Duration.between(examinee.lastOnline, LocalDateTime.now()).toSeconds();
        if(seconds > (interval* 3L)) {
            return false;
        }
        return true;
    }
}
