package at.htl.boundary;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
//import org.jboss.resteasy.annotations.jaxrs.PathParam;
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
    @Path("/enroll/{id}/{firstName}/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    Long enrollStudentForExam(@PathParam("id") String id,
                                       @PathParam("firstName") String firstName,
                                       @PathParam("lastName") String lastName);

    @GET
    @Path("verifyPIN/{pin}")
    @Produces(MediaType.APPLICATION_JSON)
    Long verifyPIN(@PathParam("pin") String pin);

}
