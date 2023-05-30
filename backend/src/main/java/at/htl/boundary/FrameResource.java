package at.htl.boundary;

import at.htl.service.FrameService;
import com.aayushatharva.brotli4j.decoder.DecoderJNI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/frame")
public class FrameResource {

    private static final String ALPHA_FRAMES_DIR = "alpha-frames/";


    @Inject
    FrameService frameService;

    @Path("alpha")
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response saveAlphaFrame(byte[] imageData) {
        var imageSaved = frameService.saveAlphaFrame(imageData);
        return Response.status(imageSaved ?
                        Response.Status.OK :
                        Response.Status.INTERNAL_SERVER_ERROR)
                .build();
    }

    @POST
    @Path("/beta")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response sendBetaFrame(byte[] file) {
        frameService.mergeWithAlphaFrame(file);
        return Response.status(Response.Status.OK).build();

    }


}