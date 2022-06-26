package at.htl.boundary;

import at.htl.control.ExaminerRepository;
import at.htl.entity.Examiner;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/examiners")
public class ExaminerResource {

    @Inject
    ExaminerRepository examinerRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Examiner> getAllExaminersOrderByName() {
        return examinerRepository.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examiner postExaminer(Examiner examiner) {
        Examiner e = new Examiner(examiner.userName, examiner.firstName, examiner.lastName, examiner.isAdmin);
        examinerRepository.persist(e);
        Log.info("Saved Examiner: " + e.lastName);
        return e;
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examiner deleteExaminer(@PathParam("id") Long id) {
        //only possible for admin
        Examiner examiner = examinerRepository.findById(id);
        examinerRepository.deleteById(id);
        Log.info("Delete Examiner: " + examiner.userName);
        return examiner;
    }
}
