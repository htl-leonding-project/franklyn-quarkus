package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Examinee;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExamineeRepository implements PanacheRepository<Examinee> {
}
