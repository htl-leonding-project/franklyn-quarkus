package at.htl.boundary.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@RegisterRestClient(configKey="webuntis")
public interface WebUntisService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    Response uploadFile(File file, @QueryParam("filename") String fileName, @QueryParam("examineeId") Long examineeId);


}
