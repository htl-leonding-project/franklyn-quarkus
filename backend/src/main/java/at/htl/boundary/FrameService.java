package at.htl.boundary;


import io.vertx.ext.web.RoutingContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;

@Path("/live-image")
public class FrameService {

    @GET
    @Path("{test}/{student}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getImage(@PathParam("test") String test, @PathParam("student") String student) {
        System.out.println(URI.create("http://localhost:8082/api/live-image/" + test + "/" + student));
        return Response.temporaryRedirect(URI.create("http://localhost:8082/api/live-image/" + test + "/" + student)).build();
    }
}
