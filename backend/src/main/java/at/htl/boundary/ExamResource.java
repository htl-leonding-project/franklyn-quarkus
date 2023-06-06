package at.htl.boundary;

import at.htl.control.*;
import at.htl.entity.*;
import at.htl.entity.dto.*;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Parameters;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Path("exams")
public class ExamResource {
    @Inject
    ExamRepository examRepository;
    @ConfigProperty(name = "CURRENT_ROOT_DIRECTORY")
    File root;
    @Inject
    UserGroupRepository userGroupRepository;
    @Inject
    UserSessionRepository userSessionRepository;
    @Inject
    UserRepository userRepository;
    @GET
    @Transactional
    @Path("/examiner/{adminId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShowExamDto> getExamsByExaminerId(@PathParam("adminId") String adminId) {
        List<Exam> exams = examRepository.find("select e from Exam e where e.adminId = :id and e.isDeleted = false order by e.date desc", Parameters.with("id", Long.valueOf(adminId))).list();

        List<ShowExamDto> examSummary = new LinkedList<>();

        for (Exam exam : exams) {
            BuildShowExamDTO(adminId, examSummary, exam);
        }
        return examSummary;
    }

    private void BuildShowExamDTO(String adminId, List<ShowExamDto> examSummary, Exam exam) {
        var examiners = examRepository.GetAllExaminersOfExam(exam, Long.parseLong(adminId));
        var forms = examRepository.GetAllSchoolClassesOfExam(exam);
        var nrOfStudentsPerExam = this.userSessionRepository.getCountOfExamineesByExamId(exam.id);
        boolean canBeDeleted = false;
        boolean canBeEdited = false;

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
            canBeEdited = true;
            canBeDeleted = true;
        } else {
            status = "Beendet";
            canBeDeleted = true;
        }

        // Dto wird zusammengesetzt
        examSummary.add(new ShowExamDto(
                exam.title, exam.date.toString(), examiners, forms,
                exam.startTime != null ? exam.startTime.toString() : " ",
                Integer.toString(nrOfStudentsPerExam),
                status, exam.pin, exam.id,
                exam.date.equals(LocalDate.now()),
                canBeEdited,
                canBeDeleted,
                exam.interval));
    }

    @GET
    @Path("/latestExam/examiner/{adminId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ShowExamDto getLatestExamByExaminerId(@PathParam("adminId") Long adminId) {
        List<ShowExamDto> examSummary = getExamsByExaminerId(String.valueOf(adminId));
        if(examSummary.size() == 0) {
            return null;
        }
        return examSummary.get(0);
    }

    @GET
    @Path("/exam/{examId}/examiner/{adminId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ShowExamDto getExamById(@PathParam("examId") String examId, @PathParam("adminId") String adminId) {
        Exam exam = examRepository.findById(Long.parseLong(examId));
        if(exam == null) {
            return null;
        }
        if (exam.isDeleted) {
            return null;
        }
        var examSummary = new LinkedList<ShowExamDto>();
        BuildShowExamDTO(adminId, examSummary, exam);
        return examSummary.get(0);
    }
/*
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

        e.userExaminers = new LinkedList<>();
        for (String examinerId : exam.examinerIds()) {
            User user = userRepository.findById(Long.parseLong(examinerId));
            if (user != null) {
                e.userExaminers.add(user);
                UserSession userSession = new UserSession(user, e, UserRole.EXAMINEE);
                userSession.exam.create(e);
            }
        }

        e.userSessions = new LinkedList<>();
        for (String formId : exam.formIds()) {
            UserSession userSession = userSessionRepository.findById(Long.parseLong(formId));
            if (userSession != null) {
                e.userSessions.add(userSession);
                userSession.exam.add(e);
            }
        }
        examRepository.persist(e);
        return pin;
    }*/

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Exam addExam(ExamDto exam) {
        Exam e = new Exam(exam.pin(), exam.title(), exam.state(),exam.date(), exam.startTime(), exam.endTime(), exam.interval());
        examRepository.persist(e);
        Log.info("Saved Exam: " + e.title);
        return e;
    }


    @PUT
    @Path("addExaminer/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Exam addUserToExam(@PathParam("id") Long id, UserDto user) {
        Exam exam = examRepository.findById(id);
        User newUser = new User(user.firstName(), user.lastName(), true);
        exam.userExaminers.add(newUser);
        return exam;
    }

    @DELETE
    @Path("delete/{examId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Exam deleteExam(@PathParam("examId") Long examId) {
        Exam exam = examRepository.findById(examId);
        if (exam == null)
            return null;
        userSessionRepository.deleteExamFromExaminers(examId);

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
        List<User> users = new ArrayList<>();
        List<UserGroup> userGroups = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (exam == null)
            return null;

        for (var examiner : updatedExam.examinerIds()) {
            users.add(userRepository.findById(Long.valueOf(examiner)));
        }

        for (var form : updatedExam.formIds()) {
            userGroups.add(userGroupRepository.findById(Long.valueOf(form)));
        }

        exam.title = updatedExam.title();
        exam.date = LocalDate.parse(updatedExam.date(), formatter);
        exam.interval = updatedExam.interval();
        exam.userExaminers = users;
        exam.userGroups = userGroups;

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
        User user = new User(firstName, lastName,true);
        boolean examineeAlreadyExists = userSessionRepository.checkIfAlreadyEnrolled(user.firstName, user.lastName, exam.id);
        if (examineeAlreadyExists) {
            return -1L;
        }
        userRepository.persist(user);
        User returnUser = userRepository.find("id", user.id).firstResult();
        if (returnUser == null)
            return 0L;
        return returnUser.id;
    }

    @GET
    @Path("enroll/again/{id}/{firstName}/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Long enrollStudentForExamAgain(@PathParam("id") Long id,
                                          @PathParam("firstName") String firstName,
                                          @PathParam("lastName") String lastName) {

        User user = userRepository.findByName(lastName, firstName);
        if(user == null)
            return 0L;
        return user.id;
    }

    @GET
    @Transactional
    @Path("getIntervalByExamId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int getIntervalByExam(@PathParam("id") Long id){
        int interval = examRepository.getIntervalByExamId(id);

        Log.info(interval);
        return interval;
    }

    // Tran muss definitiv überarbeiten => falsche entitäten, passt nichts zusammen


}
