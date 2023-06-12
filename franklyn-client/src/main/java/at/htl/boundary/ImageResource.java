package at.htl.boundary;

import at.htl.control.ApiCalls;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Path("/getActualImage")
public class ImageResource {
    @Inject
    ApiCalls apiCalls;


    ;

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getActualImage() throws IOException {
        var image = apiCalls.getNewBufferedImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        var response = Response.ok(baos.toByteArray());
        response.header("Content-Disposition", "attachment;filename=file.jpg");
        return response.build();

    }
}
