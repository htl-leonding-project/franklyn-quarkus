package at.htl.boundary;

import at.htl.service.WebUntisService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("api/webuntis")
public class WebUntisResource {

    @Inject
    WebUntisService webUntisService;

    @GET
    @Path("authUser/{userName}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    public String authUser(@PathParam("userName") String userName, @PathParam("password") String password){
        String response = webUntisService.authenticateUser(userName, password);
        return response;
    }
}
