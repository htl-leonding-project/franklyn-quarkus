package at.htl.boundary;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("upload")
@RegisterRestClient(configKey="file-upload-api")
public interface ImageService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    Response uploadFile(File file, @QueryParam("filename") String fileName);
}
