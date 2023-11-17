package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExaminerRepository;
import at.htl.entity.Exam;
import at.htl.entity.Examiner;
import at.htl.entity.User;
import at.htl.entity.dto.ExaminerDto;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Path("examiners")
public class ExaminerResource {
    @Inject
    ExaminerRepository examinerRepository;
    @Inject
    ExamRepository examRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Examiner> listAll() {
        return examinerRepository.list("select ex from Examiner ex order by ex.lastName");
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
        username = username.toUpperCase();
        Examiner examiner = examinerRepository.findByUsername(username);
        return examiner;
    }


    @GET
    @Path("exam/{examId}/examiner/{examinerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<User> getExaminerByExamId(@PathParam("examId") Long examId, @PathParam("examinerId") Long examinerId) {
      /*  Exam exam = examRepository.findById(examId);
        var examiners = exam.userExaminers();
        for (User examiner : examiners) {
            if (Objects.equals(examiner.id, examinerId)) {
                examiners.remove(examiner);
                break;
            }
        }
        return examiners;*/
        return null;
    }

    @GET
    @Path("getById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examiner getExaminerById(@PathParam("id") Long id) {
        return examinerRepository.findById(Long.parseLong(String.valueOf(id)));
    }
}
