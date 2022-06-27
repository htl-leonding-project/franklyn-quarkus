package at.htl.boundary;

import io.quarkus.logging.Log;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("api/exams")
@RegisterRestClient(configKey="login-examinee-api")
public interface ExamineeService {

    @GET
    @Path("enroll/{id}/{firstName}/{lastName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Long enrollStudentForExam(@PathParam("id") Long id, @PathParam("firstName") String firstName,@PathParam("lastName") String lastName);

    @GET
    @Path("verifyPIN/{pin}")
    @Produces(MediaType.APPLICATION_JSON)
    public Long verifyPIN(@PathParam("pin") String pin);

}
