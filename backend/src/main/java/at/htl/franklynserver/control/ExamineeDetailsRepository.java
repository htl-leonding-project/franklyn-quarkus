package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.ExamineeDetails;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
@ApplicationScoped
public class ExamineeDetailsRepository implements PanacheRepository<ExamineeDetails> {
    public Uni<List<ExamineeDetails>> getExamineeDetailsWithExamineeByID(Long id) {
        return find("#ExamineeDetails.getExamineeDetailsByExamID", id, ExamineeDetails.class).list();
    }
}
