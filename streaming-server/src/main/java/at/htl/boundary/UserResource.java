package at.htl.boundary;
import at.htl.control.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
public class UserResource {

    @Inject
    UserService userService;

    @POST
    @Path("/{exam}/{lastname}/{firstname}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(@PathParam("firstname") String firstname,@PathParam("lastname") String lastname,@PathParam("exam") String exam) {
        boolean initializeSuccsessfull = userService.initializeUser(firstname,lastname,exam);
        if (initializeSuccsessfull) {
            return Response.created(null).build();
        } else {
            return Response.ok().build( );
        }
    }
}
