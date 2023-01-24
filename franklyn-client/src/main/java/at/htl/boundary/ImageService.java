package at.htl.boundary;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

/**
 * Send Screenshots to Backend
 */

@Path("upload")
@RegisterRestClient(configKey="client-api")
public interface ImageService {

    /**
     * @param file          screenshot that gets send to backend
     * @param fileName      name of the file
     * @return              Response for the upload
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    Response uploadFile(File file, @QueryParam("filename") String fileName);
}
