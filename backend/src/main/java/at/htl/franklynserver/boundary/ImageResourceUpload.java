package at.htl.franklynserver.boundary;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.Paths;

@Path("uploadtoteacher")
@ApplicationScoped
public class ImageResourceUpload {

    @Inject
    @RestClient
    ImageService imageService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public Response uploadToTeacher(@QueryParam("filename") String filename){
        imageService.sendImage(
                Paths.get("file-upload", filename).toFile(),
                filename);

        return Response.ok().build();
    }
}
