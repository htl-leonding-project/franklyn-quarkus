package at.htl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/alphaframes")
public class AlphaFrameEndpoint {

    private static final String ALPHA_FRAMES_DIR = "alphaframes/";

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response saveAlphaFrame(byte[] imageData) {
        try {
            File alphaFramesDir = new File(ALPHA_FRAMES_DIR);
            alphaFramesDir.mkdirs();

            String fileName = generateFileName();
            String filePath = ALPHA_FRAMES_DIR + fileName;

            File imageFile = new File(filePath);
            // Save image
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageData);
            fos.close();

            return Response.status(Response.Status.OK).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String generateFileName() {
        long timestamp = System.currentTimeMillis();
        return "alphaframe_" + timestamp + ".png";
    }
}