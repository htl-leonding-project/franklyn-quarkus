package at.htl.control;

import at.htl.entity.Examiner;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExaminerRepository implements PanacheRepository<Examiner> {

}
