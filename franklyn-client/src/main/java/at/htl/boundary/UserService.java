package at.htl.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

@Path("/api/exam")
@RegisterRestClient(configKey = "exam-api")
public interface UserService {

    @GET
    @Path("enroll/{id}/{firstName}/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    Long enrollStudentForExam(@PathParam("id") String id,
                              @PathParam("firstName") String firstName,
                              @PathParam("lastName") String lastName);


    @GET
    @Path("enroll/again/{id}/{firstName}/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    Long enrollStudentForExamAgain(@PathParam("id") String id,
                                   @PathParam("firstName") String firstName,
                                   @PathParam("lastName") String lastName);

    @GET
    @Path("verifyPIN/{pin}")
    @Produces(MediaType.APPLICATION_JSON)
    Long verifyPIN(@PathParam("pin") String pin);


    @GET
    @Path("getIntervalByExamId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    int getInterval(@PathParam("id") String examId);

    @GET
    @Path("getTitleByExamId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    String getTitle(@PathParam("id") String examId);

    @GET
    @Path("getEndOfExamByExamId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    LocalDateTime getEndOfExam(@PathParam("id") String examId);
}
