package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExamineeRepository;
import at.htl.entity.Exam;
import at.htl.entity.Examinee;
import at.htl.entity.dto.ExamineeDto;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Path("api/examinees")
public class ExamineeResource {

    @Inject
    ExamineeRepository examineeRepository;

    @Inject
    ExamRepository examRepository;

    /**
     * @return list of examinees ordered by name
     */
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Examinee> listAll(){
        return examineeRepository.listAll();
    }   //todo ordering by name

    /**
     * Get Examinee by Id
     * @return examinee (if found)
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Examinee getExamineeById(@PathParam("id") Long id) {
        return examineeRepository.findById(id);
    }

    @GET
    @Path("exam/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExamineeDto> getExamineesByExamId(@PathParam("id") Long id) {
        Exam tempExam = examRepository.findById(id);
        if(tempExam == null)
            return null;
        List<Examinee> tempExaminees = examineeRepository.listAll();
        List<ExamineeDto> examinees = new LinkedList<>();
        ExamineeDto tempExaminee;
        for (Examinee examinee : tempExaminees) {
            if (Objects.equals(examinee.exam.id, id)) {
                tempExaminee = new ExamineeDto( examinee.firstName, examinee.lastName, examinee.isOnline, String.valueOf(examinee.id));
                examinees.add(tempExaminee);
            }
        }
        return examinees;
    }


    /**
     * Examinee will be updated
     * @return Updated examinee
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Examinee updateExaminee(@PathParam("id") Long id, ExamineeDto examinee) {
        Examinee ex = examineeRepository.findById(id);
        if(ex != null){
            ex.firstName = examinee.firstName();
            ex.lastName = examinee.lastName();
            ex.isOnline = examinee.isOnline();
        }
        return ex;
    }

    /**
     * Delete examinee by Id
     * @return screenshot of examinee
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examinee deleteExaminee(Long id) {
        Examinee examinee1 = examineeRepository.findById(id);
        if(examinee1 == null)
            return null;
        examineeRepository.deleteById(id);
        return examinee1;
    }

}
