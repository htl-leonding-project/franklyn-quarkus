package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Exam;
import at.htl.franklynserver.entity.Examinee;
import at.htl.franklynserver.entity.ExamineeDetails;
import at.htl.franklynserver.entity.SchoolClassDTO;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ExamRepository implements PanacheRepository<Exam> {
    public String createPIN(LocalDate date){
        Uni<List<Exam>> examqs = this.find("date", LocalDate.now()).list();
        Uni<List<Exam>> examsWithDate = this.find("date", LocalDate.now()).list();
        boolean pinIsValid = false;
        String pin;
        do{
            pin = String.valueOf((int) (Math.random()*(999-100))+100);
            Uni<List<Exam>> exams = this.find("Exam.findExamWithSameDateAndPIN", LocalDate.now(), pin).list();
            if(exams == null)
                pinIsValid = true;
        }while (pinIsValid);
        return pin;
    }
    public Uni<List<Examinee>> getExaminees(Long id) {
        return null;

    }
}
