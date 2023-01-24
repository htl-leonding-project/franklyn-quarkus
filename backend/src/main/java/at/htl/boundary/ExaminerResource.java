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

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Examiner> listAll() {
        return examinerRepository.list("select ex from Examiner ex order by ex.name");
    }

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
        return examiner;
    }

    @GET
    @Path("{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examiner getExaminerByUsername(@PathParam("username") String username) {
        Examiner examiner = examinerRepository.findByUsername(username);
        return examiner;
    }


    @GET
    @Path("exam/{examId}/examiner/{examinerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Examiner> getExaminerByExamId(@PathParam("examId") String examId, @PathParam("examinerId") String examinerId) {
        Exam exam = examRepository.findById(Long.parseLong(examId));
        var examiners = exam.examiners;
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
        return examinerRepository.findById(Long.parseLong(id));
    }


}
