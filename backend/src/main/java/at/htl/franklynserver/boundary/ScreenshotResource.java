package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ScreenshotRepository;
import at.htl.franklynserver.entity.Examiner;
import at.htl.franklynserver.entity.Screenshot;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
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
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    public Uni<Screenshot> postScreenshot(Screenshot screenshot) {
        Screenshot s = new Screenshot();
        Log.info(s.id);
        screenshotRepository.persist(s).subscribe().with(screenshot1 -> Log.info(s.id));
        return screenshotRepository.findById(screenshot.id);
    }
}
