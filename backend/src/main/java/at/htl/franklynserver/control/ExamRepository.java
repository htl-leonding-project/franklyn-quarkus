package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Exam;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExamRepository implements PanacheRepository<Exam> {


}
