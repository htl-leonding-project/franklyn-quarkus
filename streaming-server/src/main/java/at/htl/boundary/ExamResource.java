package at.htl.boundary;

import at.htl.control.ExamService;
import at.htl.entity.dto.ExamDto;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;


@Path("/exam")
public class ExamResource {


    @Inject
    ExamService examService;
    @Inject
    Logger LOG;


    @POST
    @Path("/initialize")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createExam(ExamDto exam) {
        boolean initializeSuccsessfull = examService.initializeExam(exam.title(),exam.date());
        if (initializeSuccsessfull) {
            return Response.created(null).build();
        } else {
            return Response.serverError().build();
        }
    }





}
