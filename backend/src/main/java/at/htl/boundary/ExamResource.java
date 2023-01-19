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
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Path("api/exams")
public class ExamResource {

    @Inject
    ExamRepository examRepository;

    @ConfigProperty(name = "CURRENT_ROOT_DIRECTORY")
    File root;

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
        List<String> examiners = new ArrayList<>();
        List<String> forms = new ArrayList<>();
        boolean isToday = false;
        boolean canBeEdited = true;
        boolean canBeDeleted = true;

        for (Exam exam : tempExams) {
            if(exam.isDeleted){
                continue;
            }

            if(exam.examiners != null && exam.examiners.size() > 0) {
                for(int i = 1; i < exam.examiners.size(); i++) {
                    if(exam.examiners.get(i) != null) {
                        examiners.add(exam.examiners.get(i).firstName + " " + exam.examiners.get(i).lastName);
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
            if(exam.examState == ExamState.RUNNING){
                status= "Läuft";
                canBeEdited = false;
                canBeDeleted = false;
            }
            else if ( exam.examState == ExamState.IN_PREPARATION){
                status = "In Vorbereitung";
            }
            else{
                status= "Beendet";
                canBeEdited = false;
            }
            if(exam.date.equals(LocalDate.now())){
                isToday = true;
            }
            examSummary.add(new ShowExamDto(title, date, examiners, forms, startTime, Integer.toString(nrOfStudentsPerExam), status, exam.pin, exam.id, isToday, canBeEdited, canBeDeleted, exam.interval));
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
        List<String> examiners = new ArrayList<>();
        List<String> forms = new ArrayList<>();
        boolean isToday = false;
        boolean canBeEdited = true;
        boolean canBeDeleted = true;

        for (Exam exam : tempExams) {
            if(exam.isDeleted){
                continue;
            }
            for(Examiner examiner : exam.examiners){
                if(examiner.id == Long.parseLong(id)){
                    if(exam.examiners != null && exam.examiners.size() > 0) {
                        for(int i = 0; i < exam.examiners.size(); i++) {
                            if(exam.examiners.get(i) != null) {
                                if(exam.examiners.get(i).id != Long.parseLong(id)){
                                    examiners.add(exam.examiners.get(i).firstName + " " + exam.examiners.get(i).lastName);
                                }
                            }
                        }
                    }
                    if(exam.formIds != null && exam.formIds.size() > 0) {
                        for(int i = 0; i < exam.formIds.size(); i++) {
                            if(exam.formIds.get(i) != null) {
                                forms.add(exam.formIds.get(i).title);
                            }
                        }
                    }
                    nrOfStudentsPerExam = this.examineeRepository.getCountOfExamineesByExamId(exam.id);
                    title = exam.title;
                    date= exam.date.toString();
                    if(exam.startTime != null) {
                        startTime = exam.startTime.toString();
                    }
                    else
                    {
                        startTime = " ";
                    }
                    if(exam.examState == ExamState.RUNNING){
                        status= "Läuft";
                        canBeEdited = false;
                        canBeDeleted = false;
                    }
                    else if ( exam.examState == ExamState.IN_PREPARATION){
                        status = "In Vorbereitung";
                        canBeEdited = true;
                        canBeDeleted = true;
                    }
                    else{
                        status= "Beendet";
                        canBeEdited = false;
                    }
                    if(exam.date.equals(LocalDate.now())){
                        isToday = true;
                    }
                    examSummary.add(new ShowExamDto(title, date, examiners, forms, startTime, Integer.toString(nrOfStudentsPerExam), status, exam.pin, exam.id, isToday, canBeEdited, canBeDeleted, exam.interval));
                    examiners = new ArrayList<>();
                    forms = new ArrayList<>();
                }
            }
        }
        for (int i = 0, j = examSummary.size() - 1; i < j; i++) {
            examSummary.add(i, examSummary.remove(j));
        }
        return examSummary;
    }

    @GET
    @Path("/exam/examiner/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ShowExamDto getLatestExamByExaminerId(@PathParam("id") String id) {
        List<ShowExamDto> examSummary = getExamsByExaminerId(id);
        if(examSummary.size() > 0){
            return examSummary.get(0);
        }
        return null;
    }

    @GET
    @Path("/exam/{id}/examiner/{examinerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ShowExamDto getExamById(@PathParam("id") String id, @PathParam("examinerId") String examinerId) {
        Exam exam = examRepository.findById(Long.parseLong(id));
        if(exam.isDeleted){
            return null;
        }
        boolean canBeEdited = true;
        boolean canBeDeleted = true;
        ShowExamDto examSummary;
        //StringBuilder teachers = new StringBuilder();
        //StringBuilder forms = new StringBuilder();
        List<String> examiners = new ArrayList<>();
        List<String> forms = new ArrayList<>();
        if (exam.examiners != null && exam.examiners.size() > 0) {
            for (int i = 0; i < exam.examiners.size(); i++) {
                if (exam.examiners.get(i) != null) {
                    if(exam.examiners.get(i).id != Long.parseLong(examinerId)){
                        examiners.add(exam.examiners.get(i).firstName + " " + exam.examiners.get(i).lastName);
                    }
                }
            }
        }
        if(exam.formIds != null && exam.formIds.size() > 0) {
            Log.info("FormIds: "+exam.formIds.size());
            for(int i = 0; i < exam.formIds.size(); i++) {
                if(exam.formIds.get(i) != null) {
                    forms.add(exam.formIds.get(i).title);
                }
            }
        }
        int nrOfStudentsPerExam = this.examineeRepository.getCountOfExamineesByExamId(exam.id);
        String status = "";
        if(exam.examState == ExamState.RUNNING){
            status= "Läuft";
            canBeEdited = false;
            canBeDeleted = false;
        }
        else if ( exam.examState == ExamState.IN_PREPARATION){
            status = "In Vorbereitung";
            canBeEdited = true;
            canBeDeleted = true;
        }
        else{
            status= "Beendet";
            canBeEdited = false;
            canBeDeleted = true;
        }
        String startTime = "";
        boolean isToday = false;
        if(exam.startTime != null) {
            startTime = exam.startTime.toString();
        }
        if(exam.date.equals(LocalDate.now())){
            isToday = true;
        }
        examSummary = new ShowExamDto(exam.title, exam.date.toString(), examiners, forms, startTime, Integer.toString(nrOfStudentsPerExam), status, exam.pin, exam.id, isToday, canBeEdited, canBeDeleted, exam.interval);
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
    public String addExam(ExamDto exam) {
        String pin = examRepository.createPIN(LocalDate.now());
        String tempDate = exam.date().substring(0,10);
        //String tempStartTime= tempDate + "T" + exam.startTime()+":00";
        //String tempEndTime= tempDate + "T" + exam.endTime()+":00";
        Log.info(exam.formIds().size());

        Log.info(exam.date());
        Exam e = new Exam(
                pin,
                exam.title(),
                ExamState.IN_PREPARATION,
                LocalDate.parse(tempDate),
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
                    examiner.exams.add(e);
                    //examinerRepository.getEntityManager().merge(examiner);
                    //examinerRepository.addExam(examiner, e);
                    Log.info(examiner.id);
                }
            }
        }
        List<SchoolClass> forms = new LinkedList<>();
        if(exam.formIds() != null && exam.formIds().size() > 0) {
            for (String formId : exam.formIds()) {
                SchoolClass form = schoolClassRepository.findById(Long.parseLong(formId));
                if(form != null) {
                    forms.add(form);
                    form.exams.add(e);
                    //schoolClassRepository.getEntityManager().merge(form);
                    //schoolClassRepository.addExam(form, e);
                }
            }
        }
        e.examiners = examiners;
        e.formIds = forms;
        examRepository.persist(e);
        return pin;
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
        Exam exam = examRepository.findById(id);
        if(exam == null)
            return null;
        examinerRepository.deleteExamFromExaminers(id);
        //delete screenshots
        //List<File> files = ExamRepository.deleteDirectoryOfScreenshots("Franklyn_2022-02-23", root);

        exam.examiners.clear();
        exam.formIds.clear();
        exam.pin = "";
        exam.isDeleted = true;
        examRepository.getEntityManager().merge(exam);
        return exam;
    }

    /**
     * Updates exam
     * @return updated exam
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Exam updateExam(@PathParam("id") Long id, ExamUpdateDto updatedExam) {
        Exam exam = examRepository.findById(id);
        List<Examiner> examiners = new ArrayList<>();
        List<SchoolClass> schoolClasses = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if(exam == null)
            return null;

        for(var examiner : updatedExam.examinerIds()) {
            examiners.add(examinerRepository.findById(Long.valueOf(examiner)));
        }

        for(var form : updatedExam.formIds()) {
            schoolClasses.add(schoolClassRepository.findById(Long.valueOf(form)));
        }

        exam.title = updatedExam.title();
        exam.date = LocalDate.parse(updatedExam.date(), formatter);
        exam.interval = updatedExam.interval();

        examRepository.getEntityManager().merge(exam);
        return exam;
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
            if(e.pin.equals(pin) && Objects.equals(e.date, LocalDate.now())){
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
        boolean examineeAlreadyExists = examineeRepository.checkIfAlreadyEnrolled(examinee.firstName, examinee.lastName, exam.id);
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

    @GET
    @Transactional
    @Path("getExamId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int getIntervallByExam(@PathParam("id") Long id){
        int intervall = examRepository.getIntervalByExamId(id);

        Log.info(intervall);
        return intervall;
    }

}
