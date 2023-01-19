package at.htl.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Register User to the Server
 */

@Path("api/exams")
@RegisterRestClient(configKey="client-api")
public interface ExamineeService {

    /**
     * @param id        id of the exam
     * @param firstName firstname of the student
     * @param lastName  lastname of the student
     * @return          the id if the student is enrolled
     */
    @GET
    @Path("/enroll/{id}/{firstName}/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    Long enrollStudentForExam(@PathParam("id") String id,
                                       @PathParam("firstName") String firstName,
                                       @PathParam("lastName") String lastName);

    /**
     * @param pin       pin to verify for an exam
     * @return          the examId if the pin was verified
     */
    @GET
    @Path("verifyPIN/{pin}")
    @Produces(MediaType.APPLICATION_JSON)
    Long verifyPIN(@PathParam("pin") String pin);

    @GET
    @Path("getExamId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    int getInterval(@PathParam("id") String examId);
}
