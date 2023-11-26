package at.htl.boundary;

import at.htl.control.UserGroupRepository;
import at.htl.entity.UserGroup;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/user-group")
public class UserGroupResource {
    @Inject
    UserGroupRepository userGroupRepository;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserGroup> listAll() {
        return userGroupRepository.listAll();
    }
}
