package at.htl.boundary;

import at.htl.control.ApiCalls;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

@ApplicationScoped
@ServerEndpoint("/image")
public class ImageResource {


    Session session = null;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;


    }

    @OnMessage
    public void onMessage(String message) {

        try {
            var image = apiCalls.getNewBufferedImage();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            session.getAsyncRemote().sendBinary(ByteBuffer.wrap(baos.toByteArray()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Inject
    ApiCalls apiCalls;


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
