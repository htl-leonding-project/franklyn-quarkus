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

    @PUT
    @Path("enroll/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Exam enrollStudentForExam(@PathParam("id") Long id, Examinee examinee) {
        //show if already exists with first and last name
        examineeRepository.persist(new Examinee(examinee.firstName, examinee.lastName));

        return examinee.exam;
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
