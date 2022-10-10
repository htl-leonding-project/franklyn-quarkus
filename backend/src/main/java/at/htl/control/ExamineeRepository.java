package at.htl.control;

import at.htl.entity.Exam;
import at.htl.entity.Examinee;
import at.htl.entity.Examiner;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ExamineeRepository implements PanacheRepository<Examinee> {
    public int getCountOfExamineesByExamId(Long id) {
        var query = this.getEntityManager().createQuery(
                "select e from Examinee e where e.exam.id = :id",
                Examinee.class
        ).setParameter("id", id);
        List<Examinee> examinees = query.getResultList();
        return examinees.size();
    }
}
