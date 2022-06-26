package at.htl.control;

import at.htl.entity.Examinee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExamineeRepository implements PanacheRepository<Examinee> {
}
