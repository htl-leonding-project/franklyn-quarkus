package at.htl.client;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.OutputStream;

@Path("image")
@RegisterRestClient
public interface FileService {

    @POST
    Response postFile(OutputStream os, @QueryParam("{filename}") String fileName);
}
