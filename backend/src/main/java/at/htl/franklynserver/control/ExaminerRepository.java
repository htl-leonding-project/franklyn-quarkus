package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Examiner;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExaminerRepository implements PanacheRepository<Examiner> {

}
