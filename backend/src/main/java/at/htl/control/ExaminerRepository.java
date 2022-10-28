package at.htl.control;

import at.htl.entity.Exam;
import at.htl.entity.Examiner;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;

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
        Log.info("Test");

        List<Examiner> examiners = query.getResultList();
        for (Examiner ex : examiners) {
            for (Exam e : ex.exams) {
                if (Objects.equals(e.id, id)){
                    Log.info(e.title + " deleted");
                    this.getEntityManager().remove(e);
                }
            }
        }
    }

    public Examiner findByUsername(String username) {
        List<Examiner> examiners = this.listAll();

        for (Examiner e : examiners) {
            if (e.userName.equals(username))
                return e;
        }
        return null;
    }
}
