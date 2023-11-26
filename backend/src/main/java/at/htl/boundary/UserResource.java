package at.htl.boundary;

import at.htl.control.UserRepository;
import at.htl.entity.User;
import at.htl.entity.dto.UserWebDto;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.awt.image.RescaleOp;
import java.util.List;

@Path("/user")
public class UserResource {

    @Inject
    Logger LOG;

    @Inject
    UserRepository userRepository;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAll() {
        return userRepository.listAll();
    }

    @GET
    @Path("/sign-up")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signUp(UserWebDto user) {
        boolean isAdmin = userRepository.checkMail(user.eMail());
        User u = new User(user.firstName(), user.lastName(), user.eMail(), isAdmin);
        try {
            userRepository.persist(u);
            LOG.info("User: <" + u.email + "> added " + (isAdmin ? "as Admin": ""));
            return Response.created(null).build();
        } catch (PersistenceException persistenceException) {
            LOG.info("User: <" + u.email + "> already exists");
            return Response.status(409).build();
        }
    }
}
