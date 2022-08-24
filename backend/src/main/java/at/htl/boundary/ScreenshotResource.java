package at.htl.boundary;

import at.htl.control.ScreenshotRepository;
import at.htl.entity.Screenshot;
import at.htl.entity.dto.ScreenshotDto;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("screenshot")
public class ScreenshotResource {

    @Inject
    ScreenshotRepository screenshotRepository;

    @ConfigProperty(name = "PATHOFSCREENSHOT")
    String pathOfScreenshot;

    /**
     * Looks for a specified Screenshot of an examinee
     * @return screenshot of examinee
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("number/{examineeId}/{screenshotNumber}")
    public Screenshot getScreenshotByNumber(
            @PathParam("examineeId") Long examineeId,
            @PathParam("screenshotNumber") Long screenshotNumber
    ){
        return screenshotRepository.findScreenshot(examineeId,screenshotNumber);
    }

    /**
     * Gets latest screenshot of examinee
     * @return screenshot of examinee
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("latest/{examineeId}")
    public Screenshot getLatestScreenshot(
            @PathParam("examineeId") Long examineeId
    ){
        return screenshotRepository.findLatestScreenshot(examineeId);
    }

    /**
     * Posts a new screenshot
     * @return new screenshot
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Screenshot postScreenshot(ScreenshotDto screenshot){
        Screenshot sc;
        sc = screenshotRepository.postScreenshot(screenshot);
        Log.info("Saved Screenshot: " + sc.runningNo);
        return sc;
    }
}
