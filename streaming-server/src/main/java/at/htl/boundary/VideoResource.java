package at.htl.boundary;

import at.htl.control.VideoService;
import at.htl.control.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

@Path("/videos")
public class VideoResource {

    @Inject
    VideoService videoService;

    @GET
    @Path("{testName}/{studentName}")
    public Object download(@PathParam("testName") String testName, @PathParam("studentName") String studentName){
       videoService.generateVideo(testName, studentName);
       return Response.ok().build();
    }
}
