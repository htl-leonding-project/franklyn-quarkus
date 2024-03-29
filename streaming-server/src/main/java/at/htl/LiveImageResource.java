package at.htl;

import at.htl.control.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("live-image")
public class LiveImageResource {

    @Inject
    FrameService frameService;

    @Inject
    ImageService imageService;


    @GET
    @Path("{test}/{user}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public ObjectNode getAcutalImage(@PathParam("test") String test, @PathParam("user") String user) {
        var objectMapper = new ObjectMapper();
        var json = objectMapper.createObjectNode();
        json.put("image", imageService.sendStreamImage(test, user));
        return json;
    }

}
