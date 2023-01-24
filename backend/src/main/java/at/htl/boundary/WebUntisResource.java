package at.htl.boundary;

import at.htl.service.WebUntisService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("api/webuntis")
public class WebUntisResource {

    @Inject
    WebUntisService webUntisService;

    @POST
    @Path("authUser/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    public String authUser(@PathParam("userName") String userName, String password){
        String response = webUntisService.authenticateUser(userName, password);
        return response;
    }
}
