package at.htl.control;

import at.htl.entity.Exam;
import at.htl.entity.UserSession;
import at.htl.entity.dto.ExamParticipantDTO;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ExamRepository implements PanacheRepository<Exam> {

    public String createPIN() {
        boolean pinIsValid = false;
        String pin;
        do {
            pin = String.valueOf((int) (Math.random()*(999-100))+100);
            List<Exam> exams = this.getEntityManager().createNamedQuery("Exam.findExamWithSameDateAndPIN", Exam.class)
                    .setParameter("DATE", LocalDate.now())
                    .setParameter("PIN", pin)
                    .getResultList();
            if (exams == null)
                pinIsValid = true;
        } while (pinIsValid);
        return pin;
    }




    public int getIntervalByExamId(Long id) {
        return find("#Exam.getIntervalByExamId", id).firstResult().interval;
    }

}
