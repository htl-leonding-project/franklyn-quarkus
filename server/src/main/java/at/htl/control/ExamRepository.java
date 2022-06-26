package at.htl.control;

import at.htl.entity.Exam;
import at.htl.entity.Examinee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class ExamRepository implements PanacheRepository<Exam> {
    public String createPIN(LocalDate date){
        List<Exam> examqs = this.find("date", LocalDate.now()).list();
        List<Exam> examsWithDate = this.find("date", LocalDate.now()).list();
        boolean pinIsValid = false;
        String pin;
        do{
            pin = String.valueOf((int) (Math.random()*(999-100))+100);
            List<Exam> exams = this.find("Exam.findExamWithSameDateAndPIN", LocalDate.now(), pin).list();
            if(exams == null)
                pinIsValid = true;
        }while (pinIsValid);
        return pin;
    }
    public List<Examinee> getExaminees(Long id) {
        return null;

    }
}
