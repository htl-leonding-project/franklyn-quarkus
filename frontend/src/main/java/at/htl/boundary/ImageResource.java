package at.htl.boundary;

import org.jboss.logging.Logger;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Path("/uploadImage")
public class ImageResource {
    String DIRECTORY = "./test-screenshots/";

    @GET
    @Produces("image/png")
    public Response getImage(){
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public void sentImage(InputStream is) throws IOException {
        File[] files = new File(DIRECTORY).listFiles();

        if(files != null){
            for (File f:
                    files) {
                try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
                    is.transferTo(new FileOutputStream(f));
                }

                int read = 0;
                byte[] bytes = new byte[1024];

                try(OutputStream os = new FileOutputStream(f)){
                    while((read = is.read(bytes)) != -1){
                        os.write(bytes, 0, read);
                    }
                    os.flush();
                }

                try{
                    Files.copy(is, Paths.get(f.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
                } finally {
                    is.close();
                }
            }
        }
    }
}
