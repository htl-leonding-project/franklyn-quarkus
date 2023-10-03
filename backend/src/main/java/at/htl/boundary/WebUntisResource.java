package at.htl.boundary;

import at.htl.control.ExaminerRepository;
import at.htl.entity.Examiner;
import at.htl.service.WebUntisService;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("webuntis")
public class WebUntisResource {

    @Inject
    WebUntisService webUntisService;

    @Inject
    ExaminerRepository examinerRepository;

    @POST
    @Path("authUser/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean authUser(@PathParam("userName") String userName, String password){
        boolean response = webUntisService.authenticateUser(userName, password);
        userName = userName.toUpperCase();
        Examiner examiner = examinerRepository.findByUsername(userName);
        if(response && examiner != null){
            return true;
        }
        return false;
    }
}
