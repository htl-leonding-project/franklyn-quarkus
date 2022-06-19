package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ScreenshotRepository;
import at.htl.franklynserver.entity.Screenshot;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("screenshot")
public class ScreenshotResource {

    @Inject
    ScreenshotRepository screenshotRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("number/{examineeId}/{screenshotNumber}")
    public Uni<Screenshot> getScreenshotByNumber(
            @PathParam("examineeId") int examineeId,
            @PathParam("screenshotNumber") int screenshotNumber
    ){
        return screenshotRepository.findScreenshot(examineeId,screenshotNumber);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("latest/{examineeId}")
    public Uni<Screenshot> getLatestScreenshot(
            @PathParam("examineeId") Long examineeId
    ){
        return screenshotRepository.findLatestScreenshot(examineeId);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Screenshot> postScreenshot(Screenshot screenshot) {
        return screenshotRepository.postScreenshot(screenshot);
    }
}
