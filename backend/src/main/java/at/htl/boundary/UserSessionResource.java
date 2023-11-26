package at.htl.boundary;

import at.htl.control.UserSessionRepository;
import at.htl.entity.UserSession;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/user-session")
public class UserSessionResource {

    @Inject
    UserSessionRepository userSessionRepository;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserSession> getAll() {
        return userSessionRepository.listAll();
    }


}
