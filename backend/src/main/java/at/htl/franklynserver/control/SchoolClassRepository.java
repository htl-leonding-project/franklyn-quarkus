package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.SchoolClass;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SchoolClassRepository implements PanacheRepository<SchoolClass> {
}
