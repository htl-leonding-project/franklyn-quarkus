package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ScreenshotRepository;
import at.htl.control.UserRepository;
import at.htl.control.UserSessionRepository;
import at.htl.entity.*;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Path("upload") //TODO: CHANGE URL IN CLIENT (add /api)
public class ImageResource {
    @Inject
    Logger LOG;

    @Inject
    ScreenshotRepository screenshotRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    UserSessionRepository userSessionRepository;

    @ConfigProperty(name = "PATHOFSCREENSHOT")
    String pathOfScreenshots;

    Long cnt = 0L;

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Multi<Response> download(InputStream is, @QueryParam("filename") String filename) throws IOException {

        if (filename.isBlank()) {
            filename = "unknown.xxx";
            LOG.error("filename is empty");
        }

        try (is) {
            String[] fullPath = filename.split("_|\\.");
            Exam exam = examRepository.findById(Long.valueOf(fullPath[3]));
            exam.examState = ExamState.RUNNING;
            exam.startTime = LocalDateTime.now();
            java.nio.file.Path path =
                    Paths.get(String.format("../../%s/%s_%s/%s_%s",
                            pathOfScreenshots,
                            exam.title,
                            exam.date,
                            fullPath[1],
                            fullPath[2]));
            Files.createDirectories(path);

            Files.copy(
                    is,
                    Paths.get(path.toString(), filename),
                    StandardCopyOption.REPLACE_EXISTING
            );
            cnt++;
            //String[] files = filename.split("_");
            //LOG.info(files[1]);
            //Exam exam = examRepository.findById(Long.valueOf(files[3]));
            examRepository.persist(exam);
            UserSession userSession = userSessionRepository.findByName(exam.id, fullPath[1], fullPath[2]);
            userSession.getUser().isOnline = true;
            userSession.getUser().lastOnline = LocalDateTime.now();
            userSessionRepository.persist(userSession);
            Screenshot screenshot = new Screenshot(
                    Timestamp.valueOf(LocalDateTime.now()),
                    cnt,
                    userSession.getUser(),
                    pathOfScreenshots+"/"+ exam.title+"_"+ exam.date +"/"+fullPath[1]+"_"+fullPath[2]+"/"+filename,
                    exam
            );
            screenshotRepository.post(screenshot);
        }catch (NullPointerException ignore) {
            LOG.error("Error while saving the file");
        }

        return Multi.createFrom().items(Response.ok().build());
    }
}