/*
package at.htl.client;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Set;

@Path("/extension")
public class ExtensionsResource {
    @Inject
    @RestClient
    ExtensionsService extensionsService;

    @POST
    @Path("/file")
    public Set<Extension> fileName(@QueryParam("{filename}") String fileName) {
        return extensionsService.getByFilename(fileName);
    }
}
*/
