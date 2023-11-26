package at.htl.control;

import at.htl.entity.Exam;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExamRepository implements PanacheRepository<Exam> {
}
