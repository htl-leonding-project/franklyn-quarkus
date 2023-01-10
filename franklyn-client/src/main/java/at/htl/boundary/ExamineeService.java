package at.htl.boundary;

import io.smallrye.mutiny.Uni;
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
     * Returns
     *      -1L ... student already exists (not enrolled)
     *      <<StudentId>> ... student got persisted (is now enrolled)
     * @param id Id of the exam
     * @param firstName firstname of the student
     * @param lastName lastname of the student
     */
    @GET
    @Path("/enroll/{id}/{firstName}/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    Long enrollStudentForExam(@PathParam("id") String id,
                                       @PathParam("firstName") String firstName,
                                       @PathParam("lastName") String lastName);

    /**
     * Returns
     *      0L ... exam was not found
     *      <<ExamId>>> exam was found
     * @param pin pin to verify for an exam
     */
    @GET
    @Path("verifyPIN/{pin}")
    @Produces(MediaType.APPLICATION_JSON)
    Long verifyPIN(@PathParam("pin") String pin);

}
