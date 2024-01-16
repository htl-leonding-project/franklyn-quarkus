package at.htl.boundary;

import at.htl.control.ImageService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/img")
public class ImageResource {

    @Inject
    ImageService imageService;

    @POST
    @Path("{testname}/{student}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response saveImage(@PathParam("testname") String test, @PathParam("student") String student, byte[] imageData) {
        var imageSaved = imageService.saveFrame(imageData);
        return Response.status(imageSaved ?
                        Response.Status.OK :
                        Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }


}
