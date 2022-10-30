package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExamineeRepository;
import at.htl.control.ExaminerRepository;
import at.htl.control.SchoolClassRepository;
import at.htl.entity.*;
import at.htl.entity.dto.ExamDto;
import at.htl.entity.dto.ExamUpdateDto;
import at.htl.entity.dto.ExaminerDto;
import at.htl.entity.dto.ShowExamDto;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Inject
    SchoolClassRepository schoolClassRepository;

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
        String status = "";
        List<String> teachers = new LinkedList<>();
        List<String> forms = new LinkedList<>();

        for (Exam exam : tempExams) {

            if(exam.examiners != null && exam.examiners.size() > 0) {
                for(int i = 1; i < exam.examiners.size(); i++) {
                    if(exam.examiners.get(i) != null) {
                        teachers.add(exam.examiners.get(i).firstName + " " + exam.examiners.get(i).lastName);
                    }
                }
            }
            if(exam.formIds != null && exam.formIds.size() > 0) {
                for(int i = 1; i < exam.formIds.size(); i++) {
                    if(exam.formIds.get(i) != null) {
                        forms.add(exam.formIds.get(i).title);
                    }
                }
            }
            nrOfStudentsPerExam = this.examineeRepository.getCountOfExamineesByExamId(exam.id);
            title = exam.title;
            date= exam.date.toString();
            startTime = exam.startTime.toString();
            if(exam.ongoing){
                status= "Läuft";
            }
            else{
                status= "Beendet";
            }
            examSummary.add(new ShowExamDto(title, date, teachers, forms, startTime, Integer.toString(nrOfStudentsPerExam), status, exam.pin, exam.id));
        }
        for (int i = 0, j = examSummary.size() - 1; i < j; i++) {
            examSummary.add(i, examSummary.remove(j));
        }
        return examSummary;
    }

    @GET
    @Path("/examiner/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShowExamDto> getExamsByExaminerId(@PathParam("id") String id) {
        List<Exam> tempExams = examRepository.listAll();
        List<ShowExamDto> examSummary = new LinkedList<>();
        int nrOfStudentsPerExam = 0;
        String title = "";
        String date = "";
        String startTime = "";
        String status = "";
        List<String> teachers = new LinkedList<>();
        List<String> forms = new LinkedList<>();

        for (Exam exam : tempExams) {
            for(Examiner examiner : exam.examiners){
                if(examiner.id == Long.parseLong(id)){
                    if(exam.examiners != null && exam.examiners.size() > 0) {
                        for(int i = 1; i < exam.examiners.size(); i++) {
                            if(exam.examiners.get(i) != null) {
                                teachers.add(exam.examiners.get(i).firstName + " " + exam.examiners.get(i).lastName);
                            }
                        }
                    }
                    if(exam.formIds != null && exam.formIds.size() > 0) {
                        for(int i = 1; i < exam.formIds.size(); i++) {
                            if(exam.formIds.get(i) != null) {
                                forms.add(exam.formIds.get(i).title);
                            }
                        }
                    }
                    nrOfStudentsPerExam = this.examineeRepository.getCountOfExamineesByExamId(exam.id);
                    title = exam.title;
                    date= exam.date.toString();
                    startTime = exam.startTime.toString();
                    if(exam.ongoing){
                        status= "Läuft";
                    }
                    else{
                        status= "Beendet";
                    }
                    examSummary.add(new ShowExamDto(title, date, teachers, forms, startTime, Integer.toString(nrOfStudentsPerExam), status, exam.pin, exam.id));
                }
            }
        }
        for (int i = 0, j = examSummary.size() - 1; i < j; i++) {
            examSummary.add(i, examSummary.remove(j));
        }
        return examSummary;
    }


/*    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ShowExamDto getById(@PathParam("id") String id) {
        Exam temp = examRepository.findById(Long.parseLong(id));
        ShowExamDto examSummary;
        String secondTeacher = temp.examiners.get(0).firstName + " " + temp.examiners.get(0).lastName;
        String form = temp.formIds.get(0).title;
        int nrOfStudentsPerExam = 0;

        examSummary = new ShowExamDto(temp.title, temp.date.toString(), secondTeacher, form, temp.startTime.toString(), Integer.toString(nrOfStudentsPerExam), temp.ongoing, temp.pin, temp.id);
        return examSummary;
    }*/





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
        String tempStartTime= tempDate + "T" + exam.startTime()+":00";
        String tempEndTime= tempDate + "T" + exam.endTime()+":00";


        Log.info(exam.date());
        Exam e = new Exam(
                pin,
                exam.title(),
                true,
                LocalDate.parse(tempDate),
                LocalDateTime.parse(tempStartTime),
                LocalDateTime.parse(tempEndTime),
                5,
                Resolution.HD,
                1
        );
        List<Examiner> examiners = new LinkedList<>();
        if(exam.examinerIds() != null && exam.examinerIds().size() > 0) {
            for (String examinerId : exam.examinerIds()) {
                Log.info(examinerId);
                Examiner examiner = examinerRepository.findById(Long.parseLong(examinerId));
                if(examiner != null) {
                    examiners.add(examiner);
                    Log.info(examiner.id);
                }
            }
        }
        List<SchoolClass> forms = new LinkedList<>();
        if(exam.formIds() != null && exam.formIds().size() > 0) {
            for (String formId : exam.formIds()) {
                SchoolClass form = schoolClassRepository.findById(Long.parseLong(formId));
                forms.add(form);
            }
        }

        e.examiners = examiners;
        e.formIds = forms;
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
        List<Exam> exams = examRepository.listAll();
        Exam exam = null;
        boolean check = false;
        for (Exam e : exams) {
            if(e.pin.equals(pin)){
                exam = e;
                check = true;
            }
        }
        if (!check){
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
        boolean examineeAlreadyExists = examineeRepository.checkIfAlreadyEnroled(examinee.firstName, examinee.lastName, exam.id);
        if(examineeAlreadyExists){
            return -1L;
        }
        examineeRepository.persist(examinee);
        Examinee returnExaminee = examineeRepository.find("id", examinee.id).firstResult();
        if(returnExaminee == null)
            return 0L;
        Log.info(returnExaminee.id);
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
