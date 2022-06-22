package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ExaminerRepository;
import at.htl.franklynserver.entity.Examiner;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/examiners")
public class ExaminerResource {

    @Inject
    ExaminerRepository examinerRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Examiner>> getAllExaminersOrderByName() {
        return examinerRepository.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    public Uni<Examiner> postExaminer(Examiner examiner) {
        Examiner e = new Examiner(examiner.userName, examiner.firstName, examiner.lastName, examiner.isAdmin);
        examinerRepository.persist(e).subscribe().with(examiner1 -> Log.info(examiner1.lastName));
        return examinerRepository.find("lastName", e.lastName).firstResult();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    public Uni<Examiner> deleteExaminer(@PathParam("id") Long id) {
        //only possible for admin
        Uni<Examiner> examiner = examinerRepository.findById(id);
        examinerRepository.deleteById(id)
                .subscribe().with(e ->{Log.info(id);});
        return examiner;
    }
}
