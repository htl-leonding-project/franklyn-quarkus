package at.htl.boundary;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public class ImageResource {
    @GET
    @Produces("image/png")
    public Response getImage(){
        return Response.ok().build();
    }
}
