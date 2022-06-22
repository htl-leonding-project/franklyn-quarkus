package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ScreenshotRepository;
import at.htl.franklynserver.entity.*;
import at.htl.franklynserver.entity.dto.ScreenshotDto;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Path("screenshot")
public class ScreenshotResource {

    @Inject
    ScreenshotRepository screenshotRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("number/{examineeId}/{screenshotNumber}")
    public Uni<Screenshot> getScreenshotByNumber(
            @PathParam("examineeId") Long examineeId,
            @PathParam("screenshotNumber") Long screenshotNumber
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    public Uni<Screenshot> postScreenshot(ScreenshotDto screenshot){
        Date date = new Date();
        Screenshot s = new Screenshot(
                new Timestamp(date.getTime()),
                screenshot.runningNo(),
                screenshot.exam(),
                screenshot.examinee(),
                Resolution.HD,
                1
        );
        screenshotRepository.persist(s).subscribe().with(screenshot1 -> Log.info(s.runningNo));
        return null;
    }
}
