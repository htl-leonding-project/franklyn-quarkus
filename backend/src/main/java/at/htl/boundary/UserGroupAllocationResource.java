package at.htl.boundary;

import at.htl.control.UserGroupAllocationRepository;
import at.htl.entity.UserGroupAllocation;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/user-group-allocation")

public class UserGroupAllocationResource {

    @Inject
    UserGroupAllocationRepository userGroupAllocationRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserGroupAllocation> getAll() {
        return userGroupAllocationRepository.listAll();
    }

}
