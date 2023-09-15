package at.htl.boundary;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@RegisterRestClient(configKey = "client-api")
public interface FrameService {

    @POST
    @Path("frame/alpha")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    Response saveAlphaFrame(byte[] imageData);


    @POST
    @Path("frame/beta")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    Response sendBetaFrame(byte[] file);
}
