package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExaminerRepository;
import at.htl.entity.Exam;
import at.htl.entity.Examiner;
import at.htl.entity.dto.ExaminerDto;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("api/examiners")
public class ExaminerResource {

    @Inject
    ExaminerRepository examinerRepository;

    @Inject
    ExamRepository examRepository;

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
     * Only possible for admin
     * @return deleted examiner
     */
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examiner deleteExaminer(@PathParam("id") Long id) {
        Examiner examiner = examinerRepository.findById(id);
        if(examiner == null)
            return null;
        examRepository.deleteExaminerFromExams(id);
        examinerRepository.deleteById(id);
        Log.info("Delete Examiner: " + examiner.userName);
        return examiner;
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examiner getExaminerByUsername(@PathParam("username") String username) {
        Examiner examiner = examinerRepository.findByUsername(username);
        if(examiner == null)
            return null;
        return examiner;
    }


    @GET
    @Path("exam/id/{id}/examiner/{examinerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Examiner> getExaminerByExamId(@PathParam("id") String id, @PathParam("examinerId") String examinerId) {
        Exam exam = examRepository.findById(Long.parseLong(id));
        List<Examiner> examiners = new ArrayList<>();
        examiners = exam.examiners;
        for (Examiner examiner : examiners) {
            if (examiner.id == Long.parseLong(examinerId)) {
                examiners.remove(examiner);
                break;
            }
        }
        return examiners;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examiner getExaminerById(@PathParam("id") String id) {
        Examiner examiner = examinerRepository.findById(Long.parseLong(id));
        if(examiner == null)
            return null;
        return examiner;
    }


}
