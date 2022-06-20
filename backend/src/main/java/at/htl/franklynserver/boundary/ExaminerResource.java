package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ExaminerRepository;
import at.htl.franklynserver.entity.Examiner;
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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Examiner> postExaminer(Examiner examiner) {
        //webuntis verification
        examinerRepository.persist(examiner);
        return examinerRepository.findById(examiner.id);
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Examiner> deleteExaminer(@PathParam("id") Long id) {
        //only possible for admin
        Uni<Examiner> examiner = examinerRepository.findById(id);
        if(examiner != null) {
            examinerRepository.deleteById(id);
        }
        return examiner;
    }
}
