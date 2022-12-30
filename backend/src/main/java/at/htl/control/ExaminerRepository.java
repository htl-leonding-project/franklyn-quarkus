package at.htl.control;

import at.htl.entity.Exam;
import at.htl.entity.Examiner;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ExaminerRepository implements PanacheRepository<Examiner> {

    @Inject
    ExamRepository examRepository;

    public void deleteExamFromExaminers(Long id) {
        var query = this.getEntityManager().createQuery(
                "select e from Examiner e",
                Examiner.class
        );

        List<Examiner> examiners = query.getResultList();

        for (Examiner examiner : examiners) {
            examiner.exams.removeIf(exam -> Objects.equals(exam.id, id));
        }

        Exam exam = examRepository.findById(id);
        exam.examiners.clear();
        examRepository.getEntityManager().merge(exam);
        examRepository.flush();
    }

    public Examiner findByUsername(String username) {
        List<Examiner> examiners = this.listAll();

        for (Examiner e : examiners) {
            if (e.userName.equals(username))
                return e;
        }
        return null;
    }

/*    public void addExam(Examiner examiner, Exam e) {
        examiner.exams.add(e);
        this.getEntityManager().merge(examiner);
    }*/
}
