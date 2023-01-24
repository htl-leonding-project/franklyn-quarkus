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
import io.quarkus.panache.common.Parameters;
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

    @GET
    @Transactional
    @Path("/examiner/{adminId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShowExamDto> getExamsByExaminerId(@PathParam("adminId") String adminId) {
        List<Exam> exams = examRepository.find("select e from Exam e where e.adminId = :id and e.isDeleted = false order by e.date", Parameters.with("id", Long.valueOf(adminId))).list();

        List<ShowExamDto> examSummary = new LinkedList<>();

        for (Exam exam : exams) {
            BuildShowExamDTO(adminId, examSummary, exam);
        }
        return examSummary;
    }

    private void BuildShowExamDTO(String adminId, List<ShowExamDto> examSummary, Exam exam) {
        var examiners = examRepository.GetAllExaminersOfExam(exam, Long.parseLong(adminId));
        var forms = examRepository.GetAllSchoolClassesOfExam(exam);
        var nrOfStudentsPerExam = this.examineeRepository.getCountOfExamineesByExamId(exam.id);

        // Überprüft ob Exam nicht schon beendet, wenn ja ändert sich der Status des Exams
        if (!exam.date.equals(LocalDate.now()) && exam.examState == ExamState.RUNNING) {
            exam.examState = ExamState.FINISHED;
            examRepository.persist(exam);
        }

        // Exam Status wird in Text umgewandelt
        String status = "";
        if (exam.examState == ExamState.RUNNING) {
            status = "Läuft";
        } else if (exam.examState == ExamState.IN_PREPARATION) {
            status = "In Vorbereitung";
        } else {
            status = "Beendet";
        }

        // Dto wird zusammengesetzt
        examSummary.add(new ShowExamDto(
                exam.title, exam.date.toString(), examiners, forms,
                exam.startTime != null ? exam.startTime.toString() : " ",
                Integer.toString(nrOfStudentsPerExam),
                status, exam.pin, exam.id,
                exam.date.equals(LocalDate.now()),
                exam.examState == ExamState.IN_PREPARATION,
                exam.examState == ExamState.IN_PREPARATION,
                exam.interval));
    }

    @GET
    @Path("/latestExam/examiner/{adminId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ShowExamDto getLatestExamByExaminerId(@PathParam("adminId") Long adminId) {
        List<ShowExamDto> examSummary = getExamsByExaminerId(String.valueOf(adminId));
        return examSummary.get(0) != null ? examSummary.get(0) : null;
    }

    @GET
    @Path("/exam/{examId}/examiner/{adminId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ShowExamDto getExamById(@PathParam("examId") String examId, @PathParam("adminId") String adminId) {
        Exam exam = examRepository.findById(Long.parseLong(examId));
        if (exam.isDeleted) {
            return null;
        }
        var examSummary = new LinkedList<ShowExamDto>();
        BuildShowExamDTO(adminId, examSummary, exam);
        return examSummary.get(0);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public String addExam(ExamDto exam) {
        String pin = examRepository.createPIN();
        Exam e = new Exam(
                pin,
                exam.title(),
                ExamState.IN_PREPARATION,
                LocalDate.parse(exam.date().substring(0, 10)),
                exam.interval(),
                Long.valueOf(exam.examinerIds().get(0))
        );

        e.examiners = new LinkedList<>();
        for (String examinerId : exam.examinerIds()) {
            Examiner examiner = examinerRepository.findById(Long.parseLong(examinerId));
            if (examiner != null) {
                e.examiners.add(examiner);
                examiner.exams.add(e);
            }
        }

        e.schoolClasses = new LinkedList<>();
        for (String formId : exam.formIds()) {
            SchoolClass form = schoolClassRepository.findById(Long.parseLong(formId));
            if (form != null) {
                e.schoolClasses.add(form);
                form.exams.add(e);
            }
        }

        examRepository.persist(e);
        return pin;
    }

    @PUT
    @Path("addExaminer/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Exam addExaminerToExam(@PathParam("id") Long id, ExaminerDto examiner) {
        Exam ex = examRepository.findById(id);
        Examiner newExaminer = new Examiner(examiner.userName(), examiner.firstName(), examiner.lastName(), examiner.isAdmin());
        ex.examiners.add(newExaminer);
        return ex;
    }

    @DELETE
    @Path("delete/{examId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Exam deleteExam(@PathParam("examId") Long examId) {
        Exam exam = examRepository.findById(examId);
        if (exam == null)
            return null;
        examinerRepository.deleteExamFromExaminers(examId);

        //delete screenshots
        //List<File> files = ExamRepository.deleteDirectoryOfScreenshots("Franklyn_2022-02-23", root);

        exam.pin = "";
        exam.isDeleted = true;
        examRepository.persist(exam);
        return exam;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("update/{examId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Exam updateExam(@PathParam("examId") Long examId, ExamUpdateDto updatedExam) {
        Exam exam = examRepository.findById(examId);
        List<Examiner> examiners = new ArrayList<>();
        List<SchoolClass> schoolClasses = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (exam == null)
            return null;

        for (var examiner : updatedExam.examinerIds()) {
            examiners.add(examinerRepository.findById(Long.valueOf(examiner)));
        }

        for (var form : updatedExam.formIds()) {
            schoolClasses.add(schoolClassRepository.findById(Long.valueOf(form)));
        }

        exam.title = updatedExam.title();
        exam.date = LocalDate.parse(updatedExam.date(), formatter);
        exam.interval = updatedExam.interval();
        exam.examiners = examiners;
        exam.schoolClasses = schoolClasses;

        examRepository.persist(exam);
        return exam;
    }


    @GET
    @Path("verifyPIN/{pin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Long verifyPIN(@PathParam("pin") String pin) {
        List<Exam> exams = examRepository.listAll();
        Exam exam = null;
        boolean check = false;
        for (Exam e : exams) {
            if (e.pin.equals(pin) && Objects.equals(e.date, LocalDate.now())) {
                exam = e;
                check = true;
            }
        }
        if (!check) {
            return 0L;
        }
        return exam.id;
    }

    @GET
    @Path("enroll/{examId}/{firstName}/{lastName}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Long enrollStudentForExam(@PathParam("examId") Long examId, @PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        //show if already exists with first and last name
        Exam exam = examRepository.findById(examId);
        Examinee examinee = new Examinee(firstName, lastName, exam, true, LocalDateTime.now());
        boolean examineeAlreadyExists = examineeRepository.checkIfAlreadyEnrolled(examinee.firstName, examinee.lastName, exam.id);
        if (examineeAlreadyExists) {
            return -1L;
        }
        examineeRepository.persist(examinee);
        Examinee returnExaminee = examineeRepository.find("id", examinee.id).firstResult();
        if (returnExaminee == null)
            return 0L;
        return returnExaminee.id;
    }

    // Tran muss definitiv überarbeiten => falsche entitäten, passt nichts zusammen
    @PUT
    @Transactional
    @Path("removeExaminee/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Examinee removeExamineeFromExam(@PathParam("id") Long id, Long examineeId) {
        Exam ex = examRepository.findById(id);
        ex.examinees.remove(examineeRepository.findById(examineeId));
        return examineeRepository.findById(examineeId);
    }

}
