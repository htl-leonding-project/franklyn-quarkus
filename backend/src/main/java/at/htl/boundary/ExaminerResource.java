package at.htl.boundary;

import at.htl.control.ExaminerRepository;
import at.htl.entity.Examiner;
import at.htl.entity.dto.ExaminerDto;
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

    /**
     * @return List of examinees ordereed by name(todo)
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Examiner> listAll() {        //todo ordering by name
        return examinerRepository.listAll();
    }

    /**
     * Posts new examiner
     * @return new examiner
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examiner addExaminer(ExaminerDto examiner) {
        Examiner e = new Examiner(examiner.userName(), examiner.firstName(), examiner.lastName(), examiner.isAdmin());
        examinerRepository.persist(e);
        Log.info("Saved Examiner: " + e.lastName);
        return e;
    }

    /**
     * Delete examiner by id
     * @return deleted examiner
     */
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examiner deleteExaminer(@PathParam("id") Long id) {
        //only possible for admin
        Examiner examiner = examinerRepository.findById(id);
        if(examiner == null)
            return null;
        examinerRepository.deleteById(id);
        Log.info("Delete Examiner: " + examiner.userName);
        return examiner;
    }
}
