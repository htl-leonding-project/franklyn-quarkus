package at.htl.boundary;

import org.jboss.logging.Logger;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("image")
public class ImageResource {
    String DIRECTORY = "./Test-Screenshots/";

    @Inject
    Logger LOG;

    @GET
    @Produces("image/png")
    public Response getImage(){
        return Response.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sentImage() throws IOException {
        List<BufferedImage> bImages = new ArrayList<>();
        File[] files = new File(DIRECTORY).listFiles();

        List<byte[]> images = new ArrayList<>();

        if(files != null){
            for (File f:
                    files) {
                BufferedImage bI = ImageIO.read(f);
                bImages.add(bI);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(bI, "jpg", bos);
                images.add(bos.toByteArray());
                LOG.info(bos.toByteArray());
            }
        }


        return Response.ok(images).build();
    }
}
