package at.htl.boundary;

import at.htl.control.ApiCalls;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.smallrye.common.annotation.Blocking;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@ApplicationScoped
@ServerEndpoint("/image")
public class ImageResource {

    @Inject
    Logger logger;
    @Inject
    ApiCalls apiCalls;

    Session session = null;

    ObjectMapper mapper = new ObjectMapper();

    @OnOpen
    public synchronized void onOpen(Session s) {
        this.session = s;

    }

    @OnClose
    public void onClose(Session s) {

        s.getAsyncRemote().sendText("Connection closed");
        try {
            s.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @OnError
    public void onError(Session s, Throwable throwable) {

        session.getAsyncRemote().sendText("Connection closed due to an error" + throwable.getMessage());

        try {
            session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @OnMessage
    public void onMessage(String message) {
        var json = mapper.createObjectNode();
        json.put("message", getLatestImage());

        session.getAsyncRemote().sendText(json.toPrettyString());

    }


    public String getLatestImage() {
        var image = apiCalls.getNewBufferedImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    ;

/*    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getActualImage() throws IOException {
        var image = apiCalls.getNewBufferedImage();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        var response = Response.ok(baos.toByteArray());
        response.header("Content-Disposition", "attachment;filename=file.jpg");
        return response.build();

    }*/
}
