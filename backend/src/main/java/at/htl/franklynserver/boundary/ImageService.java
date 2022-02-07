package at.htl.franklynserver.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("receive")
@RegisterRestClient(configKey="file-upload-api-server")
public interface ImageService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    Response sendImage(File file, @QueryParam("filename") String filename);
}
