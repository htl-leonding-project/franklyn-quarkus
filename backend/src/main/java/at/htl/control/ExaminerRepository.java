package at.htl.control;

import at.htl.entity.Exam;
import at.htl.entity.Examiner;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ExaminerRepository implements PanacheRepository<Examiner> {

    public void deleteExamFromExaminers(Long id) {
        var query = this.getEntityManager().createQuery(
                "select e from Examiner e",
                Examiner.class
        );
        List<Examiner> examiners = query.getResultList();
        for (Examiner ex : examiners) {
            this.deleteById(ex.id);
            for (Exam e : ex.exams) {
                if (Objects.equals(e.id, id))
                    this.getEntityManager().remove(e);
            }
        }
    }
}
