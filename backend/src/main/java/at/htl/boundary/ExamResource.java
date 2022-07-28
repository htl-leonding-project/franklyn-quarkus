package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExamineeRepository;
import at.htl.entity.Exam;
import at.htl.entity.Examinee;
import at.htl.entity.Examiner;
import at.htl.entity.Resolution;
import at.htl.entity.dto.ExamDto;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Path("api/exams")
public class ExamResource {

    @Inject
    ExamRepository examRepository;

    @Inject
    ExamineeRepository examineeRepository;


    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exam> getAll() {
        return examRepository.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Exam saveExam(ExamDto exam) {
        String pin = examRepository.createPIN(LocalDate.now());
        Exam e = new Exam(
                pin,
                exam.title(),
                true,
                LocalDate.parse(exam.date()),
                LocalDateTime.parse(exam.startTime()),
                LocalDateTime.parse(exam.endTime()),
                5,
                Resolution.HD,
                1
        );

        examRepository.persist(e);
        Log.info("Saved Exam: " + e.title);
        return e;
    }

    @PUT
    @Path("addExaminer/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Exam addExaminerToExam(@PathParam("id") Long id, Examiner examiner) {

        //validation for duplicate examiners

        Exam ex = examRepository.findById(id);
        ex.examiners.add(examiner);
        return ex;
    }

    @DELETE
    @Path("delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Exam deleteExam(@PathParam("id") Long id) {
        //can only be deleted if there are no more exaxminers in it
        Exam ex = examRepository.findById(id);
        if(ex == null)
            return null;
        examRepository.deleteById(id);
        Log.info("Delete Exam: " + ex.title);

        return ex;
    }


    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Exam updateExam(@PathParam("id") Long id, Exam exam) {
        Exam ex = examRepository.findById(id);

        ex.title = exam.title;
        ex.date = exam.date;
        ex.formIds = exam.formIds;
        ex.examiners = exam.examiners;
        ex.startTime = exam.startTime;
        ex.endTime = exam.endTime;
        ex.ongoing = exam.ongoing;
        ex.interval = exam.interval;
        ex.resolution = exam.resolution;
        ex.compression = exam.compression;

        return ex;
    }

    @GET
    @Path("verifyPIN/{pin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Long verifyPIN(@PathParam("pin") String pin){
        Log.info(pin);
        Exam exam = examRepository.getExamByPin(pin);
        if (exam == null){
            return 0L;
        }
        return exam.id;
    }

    @GET
    @Path("enroll/{id}/{firstName}/{lastName}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Long enrollStudentForExam(@PathParam("id") Long id, @PathParam("firstName") String firstName,@PathParam("lastName") String lastName) {
        //show if already exists with first and last name

        Log.info(firstName);
        Log.info(lastName);

        Exam exam = examRepository.findById(id);
        Examinee examinee = new Examinee(firstName, lastName, exam, true, LocalDateTime.now());
        Log.info(examinee.firstName);
        examineeRepository.persist(examinee);
        Examinee returnExaminee = examineeRepository.find("id", examinee.id).firstResult();
        if(returnExaminee == null)
            return 1L;
        return returnExaminee.id;
    }

    //sends details back
    @PUT
    @Transactional
    @Path("examinee/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Examinee removeExamineeFromExam(@PathParam("id") Long id, Long examineeId) {
        Exam ex = examRepository.findById(id);

        ex.examiners.remove(
                examineeRepository
                        .findById(examineeId)
        );
        return examineeRepository.findById(examineeId);
    }

}
