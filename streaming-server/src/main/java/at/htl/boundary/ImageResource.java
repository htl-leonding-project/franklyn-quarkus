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
    @Path("alpha/{testname}/{student}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAlphaImage(@PathParam("testname") String test, @PathParam("student") String student, byte[] imageData) {
        var imageSaved = imageService.saveFrame(imageData, student, test, "alpha");
        System.out.println(imageData.length);
        System.out.println("Save Alpha in Test: " + test);
        return Response.status(imageSaved ?
                        Response.Status.OK :
                        Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    @POST
    @Path("beta/{testname}/{student}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveBetaImage(@PathParam("testname") String test, @PathParam("student") String student, byte[] imageData) {
        var imageSaved = imageService.saveFrame(imageData, student, test, "alpha");
        System.out.println("Save Beta in Test: " + test);
        return Response.status(imageSaved ?
                        Response.Status.OK :
                        Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }


}
