package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExamineeRepository;
import at.htl.control.ExaminerRepository;
import at.htl.entity.Exam;
import at.htl.entity.Examinee;
import at.htl.entity.Examiner;
import at.htl.entity.Resolution;
import at.htl.entity.dto.ExamDto;
import at.htl.entity.dto.ExamUpdateDto;
import at.htl.entity.dto.ExaminerDto;
import at.htl.entity.dto.ShowExamDto;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Path("api/exams")
public class ExamResource {

    @Inject
    ExamRepository examRepository;

    @Inject
    ExaminerRepository examinerRepository;

    @Inject
    ExamineeRepository examineeRepository;

    /**
     * @return list of all exams
     */
    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShowExamDto> listAll() {
        List<Exam> tempExams = examRepository.listAll();
        List<ShowExamDto> examSummary = new LinkedList<>();
        String secondTeacher = "";
        String form = "";
        int nrOfStudentsPerExam = 0;
        String title = "";
        String date = "";
        String startTime = "";

        for (Exam exam : tempExams) {
            if(exam.examiners != null && exam.examiners.size() > 0) {
                secondTeacher = exam.examiners.get(1).firstName + " " + exam.examiners.get(1).lastName;
            }
            if(exam.formIds != null && exam.formIds.size() > 0) {
                form = exam.formIds.get(0).title;
            }
            nrOfStudentsPerExam = this.examineeRepository.getCountOfExamineesByExamId(exam.id);
            title = exam.title;
            date= exam.date.toString();
            startTime = exam.startTime.toString();
            examSummary.add(new ShowExamDto(title, date, secondTeacher, form, startTime, Integer.toString(nrOfStudentsPerExam), exam.ongoing, exam.pin));
        }
        return examSummary;
    }

    /**
     * Posts new Exam
     * @return new exam
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Exam addExam(ExamDto exam) {
        String pin = examRepository.createPIN(LocalDate.now());
        String tempDate = exam.date().substring(0,10);
        Log.info(exam.date());
        Exam e = new Exam(
                pin,
                exam.title(),
                true,
                LocalDate.parse(tempDate),
                LocalDateTime.parse(exam.startTime()),
                LocalDateTime.parse(exam.endTime()),
                5,
                Resolution.HD,
                1
        );

        examRepository.persist(e);
        return e;
    }

    /**
     * Adds an Examiner to Exam (id)
     * Verifys if examiner already has been enrolled
     * @return Exam
     */
    @PUT
    @Path("addExaminer/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Exam addExaminerToExam(@PathParam("id") Long id, ExaminerDto examiner) {

        //validation for duplicate examiners

        Exam ex = examRepository.findById(id);
        Examiner newExaminer = new Examiner(examiner.userName(), examiner.firstName(), examiner.lastName(), examiner.isAdmin());
        ex.examiners.add(newExaminer);
        return ex;
    }
    /**
     * Deletes exam
     * Remove all examiners from exam before deleting exam
     * @return screenshot of examinee
     */
    @DELETE
    @Path("delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Exam deleteExam(@PathParam("id") Long id) {
        //can only be deleted if there are no more examiners in it
        Exam ex = examRepository.findById(id);
        if(ex == null)
            return null;
        examinerRepository.deleteExamFromExaminers(id);
        Log.info("deleted exam from examiners");
        examRepository.deleteById(id);
        Log.info("Delete Exam: " + ex.title);

        return ex;
    }

    /**
     * Updates exam
     * @return updated exam
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Exam updateExam(@PathParam("id") Long id, ExamUpdateDto exam) {
        Exam ex = examRepository.findById(id);

        ex.title = exam.title();
        ex.date = LocalDate.parse(exam.date());
        ex.startTime = LocalDateTime.parse(exam.startTime());
        ex.endTime = LocalDateTime.parse(exam.endTime());
        ex.ongoing = exam.ongoing();

        return ex;
    }

    /**
     * Looks for exam with pin (for enrolling as student)
     * @return id of exam or 0 if not found
     */
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

    /**
     * Enroll student for
     * Verify if already enrolled if not -> enroll
     * if yes -> ask student if he/she wants to enroll again
     * @return screenshot of examinee
     */
    @GET
    @Path("enroll/{id}/{firstName}/{lastName}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Long enrollStudentForExam(@PathParam("id") Long id, @PathParam("firstName") String firstName,@PathParam("lastName") String lastName) {
        //show if already exists with first and last name

        Exam exam = examRepository.findById(id);
        Examinee examinee = new Examinee(firstName, lastName, exam, true, LocalDateTime.now());
        Log.info(examinee.firstName);
        examineeRepository.persist(examinee);
        Examinee returnExaminee = examineeRepository.find("id", examinee.id).firstResult();
        if(returnExaminee == null)
            return 1L;
        return returnExaminee.id;
    }


    /**
     * Removes examinee from exam
     * @return examinee
     */
    //sends details back
    @PUT
    @Transactional
    @Path("removeExaminee/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Examinee removeExamineeFromExam(@PathParam("id") Long id, Long examineeId) {
        Exam ex = examRepository.findById(id);

        ex.examiners.remove(
                examineeRepository
                        .findById(examineeId)
        );
        return examineeRepository.findById(examineeId);
    }

}
