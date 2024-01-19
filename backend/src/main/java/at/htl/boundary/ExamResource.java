package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.UserRepository;
import at.htl.control.UserSessionRepository;
import at.htl.entity.*;
import at.htl.entity.dto.ExamDto;
import at.htl.util.UtilClass;
import io.quarkus.logging.Log;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Path("/exam")
public class ExamResource {


    @Inject
    RoutingContext routingContext;
    @Inject
    Logger LOG;

    @Inject
    ExamRepository examRepository;

    @Inject
    @RestClient
    StreamingServerService streamingServerService;

    @Inject
    UserRepository userRepository;

    @Inject
    UserSessionRepository userSessionRepository;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exam> getAll() {
        return examRepository.listAll();
    }


    @GET
    @Path("/{examId}/participants")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserSession> getAllParticipantsByExamId(@Context HttpHeaders headers, @PathParam("examId") Long examId) {

        return userSessionRepository.getAllParticipantsOfExam(examId);
    }

    @GET
    @Path("{examId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Exam getExamById(@PathParam("examId") Long id) {
        return examRepository.findById(id);
    }

    @POST
    @Path("/create")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createExam(ExamDto exam) {
        String pin = examRepository.createPIN();
        Exam e = new Exam(pin, exam.title(), exam.state(), exam.date(), exam.startTime(), exam.endTime(), exam.interval());
        LOG.info("Exam: <" + e.title + "> added");
        examRepository.persist(e);
        streamingServerService.createExam(exam);
        return e.pin;
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
    public Long enrollStudentForExam(
            @PathParam("examId") Long examId,
            @PathParam("firstName") String firstName,
            @PathParam("lastName") String lastName
    ) {


        //TODO: get IP of Client and save it for Images on Frontend

        var ip = UtilClass.getIpAddress(routingContext.request());
        Exam exam = examRepository.findById(examId);
        User user = new User(firstName, lastName, true, LocalDateTime.now());

        boolean userAlreadyConnectedToExam = userSessionRepository.checkIfAlreadyPartOfExam(firstName, lastName, examId);
        if (userAlreadyConnectedToExam) {
            return -1L;
        }
        userRepository.persist(user);
        UserSession userSession = new UserSession(user, exam, UserRole.EXAMINEE, ip);
        userSessionRepository.persist(userSession);
        return user.getId();
    }

    @GET
    @Path("enroll/again/{id}/{firstName}/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Long enrollStudentForExamAgain(@PathParam("id") Long examId,
                                          @PathParam("firstName") String firstName,
                                          @PathParam("lastName") String lastName) {

        //TODO: get IP of Client and save it for Images on Frontend

        return userSessionRepository.getUserId(firstName, lastName, examId);

    }

    @GET
    @Transactional
    @Path("getIntervalByExamId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public int getIntervalByExam(@PathParam("id") Long id) {
        int interval = examRepository.getIntervalByExamId(id);
        Log.info(interval);
        return interval;
    }

    @GET
    @Transactional
    @Path("getTitleByExamId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getTitleByExam(@PathParam("id") Long id) {
        String title = examRepository.getTitleByExamId(id);
        Log.info(title);
        return title;
    }

    @GET
    @Transactional
    @Path("getEndOfExamByExamId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public LocalDateTime getEndOfExamByExam(@PathParam("id") Long id) {
        LocalDateTime endOfLesson = examRepository.getEndOfExamByExamId(id);
        return endOfLesson;
    }

    @DELETE
    @Transactional
    @Path("/{examId}/participants/{participantId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response kickStudent(
            @PathParam("examId") Long examId,
            @PathParam("participantId") Long participantId) {
        userSessionRepository.kickUser(examId, participantId);
        return Response.accepted().build();
    }

}
