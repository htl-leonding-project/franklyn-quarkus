package at.htl.boundary;

import at.htl.control.UserRepository;
import at.htl.entity.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/user")
public class UserResource {

    @Inject
    UserRepository userRepository;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAll() {
        return userRepository.listAll();
    }
}
