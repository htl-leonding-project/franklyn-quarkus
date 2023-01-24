package at.htl.boundary;

import at.htl.control.ScreenshotRepository;
import at.htl.entity.Screenshot;
import at.htl.entity.dto.ScreenshotAngularDto;
import at.htl.entity.dto.ScreenshotDto;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;

@Path("api/screenshot")
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
    @Path("latest/exam/{examId}/examinee/{examineeId}")
    public ScreenshotAngularDto getLatestScreenshot(
            @PathParam("examId") Long examId,
            @PathParam("examineeId") Long examineeId
    ){
        Screenshot temp = screenshotRepository.findLatestScreenshot(examineeId);
        return null;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("exam/{examId}/examinee/{examineeId}")
    public List<ScreenshotAngularDto> getScreenshotsOfExaminee(
            @PathParam("examId") Long examId,
            @PathParam("examineeId") Long examineeId
    ){
        List<Screenshot> screenshotsTemp = screenshotRepository.getScreenshotsOfExaminee(examId,examineeId);
        List<ScreenshotAngularDto> screenshots = new LinkedList<>();
        for (Screenshot s : screenshotsTemp) {
            screenshots.add(new ScreenshotAngularDto(s.exam.id,s.examinee.id,s.pathOfScreenshot,s.id));
        }
        for (int i = 0, j = screenshots.size() - 1; i < j; i++) {
            screenshots.add(i, screenshots.remove(j));
        }
        return screenshots;
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
