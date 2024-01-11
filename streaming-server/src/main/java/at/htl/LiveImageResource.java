package at.htl;

import at.htl.control.LiveImageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/live-image/{test}/{student}")
@ApplicationScoped
public class LiveImageResource {

    @ConfigProperty(name = "exam-directory")
    String directoryName;
    @Inject
    Logger logger;

    @Inject
    LiveImageService liveImageService;
    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("test") String testName,
            @PathParam("student") String studentName) {
        liveImageService.checkIfStudentOrTestDirectoryExist(studentName, testName, session, sessions);

    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        sessions.remove(username);
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        sessions.remove(username);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
    }
}
