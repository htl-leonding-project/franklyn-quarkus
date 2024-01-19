package at.htl;

import at.htl.control.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;

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
        json.put("image", frameService.generateStreamingFrame(test, user));
        return json;
    }

}
