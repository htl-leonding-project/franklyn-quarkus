package at.htl.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Register User to the Server
 */

@Path("api/exams")
@RegisterRestClient(configKey="client-api")
public interface ExamineeService {

    @GET
    @Path("enroll/{id}/{firstName}/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response enrollStudentForExam(@PathParam("id") Long id,
                                         @PathParam("firstName") String firstName,
                                         @PathParam("lastName") String lastName);

    @GET
    @Path("verifyPIN/{pin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response verifyPIN(@PathParam("pin") String pin);

}
