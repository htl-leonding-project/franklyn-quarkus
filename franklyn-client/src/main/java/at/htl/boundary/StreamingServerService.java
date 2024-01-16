package at.htl.boundary;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;

@Path("")
@RegisterRestClient(configKey = "streaming-server")
public interface StreamingServerService {

    @POST
    @Path("img/alpha/{testname}/{clientname}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    Response sendAlphaFrame(byte[] file, @PathParam("testname") String testname, @PathParam("clientname") String clientName);


    @POST
    @Path("img/beta/{testname}/{clientname}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    Response sendBetaFrame(byte[] file, @PathParam("testname") String testname, @PathParam("clientname") String clientName);

    @POST
    @Path("user/{testname}/{lastname}/{firstname}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    Response enrollSelf(@PathParam("testname") String testname, @PathParam("lastname") String lastname,  @PathParam("firstname") String firstname);

}
