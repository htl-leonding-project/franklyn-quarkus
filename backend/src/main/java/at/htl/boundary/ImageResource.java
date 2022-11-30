package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExamineeRepository;
import at.htl.control.ScreenshotRepository;
import at.htl.entity.Exam;
import at.htl.entity.Examinee;
import at.htl.entity.Resolution;
import at.htl.entity.Screenshot;
import at.htl.entity.dto.ScreenshotDto;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Path("upload")
public class ImageResource {
    @Inject
    Logger LOG;

    @Inject
    ScreenshotRepository screenshotRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    ExamineeRepository examineeRepository;

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

        LOG.info("Trying to save the file");
        try (is) {
            String[] fullPath = filename.split("_|\\.");
            Exam exam = examRepository.findById(Long.valueOf(fullPath[3]));
            exam.ongoing = true;
            exam.startTime = LocalDateTime.now();
            java.nio.file.Path path = Paths.get("../../"+pathOfScreenshots+"/"+ exam.title+"_"+ exam.date +"/"+fullPath[1]+"_"+fullPath[2]);
            Files.createDirectories(path);

            LOG.info(filename);
            Log.info(path.toString());
            LOG.info("saved file");

            Files.copy(
                    is,
                    Paths.get(path.toString(), filename),
                    StandardCopyOption.REPLACE_EXISTING
            );
            cnt++;
            LOG.info(filename);
            //String[] files = filename.split("_");
            //LOG.info(files[1]);
            //Exam exam = examRepository.findById(Long.valueOf(files[3]));
            examRepository.getEntityManager().merge(exam);
            Examinee examinee = examineeRepository.findByName(exam.id, fullPath[1], fullPath[2]);
            Screenshot screenshot = new Screenshot(
                    Timestamp.valueOf(LocalDateTime.now()),
                    cnt,
                    examinee,
                    Resolution.HD,
                    1,
                    "http://localhost:8082/"+pathOfScreenshots+"/"+ exam.title+"_"+ exam.date +"/"+fullPath[1]+"_"+fullPath[2]+"/"+filename,
                    exam.id
                    );
            screenshotRepository.post(screenshot);
        }catch (NullPointerException ignore) {
            LOG.error("Error while saving the file");
        }

        return Multi.createFrom().items(Response.ok().build());
    }
}