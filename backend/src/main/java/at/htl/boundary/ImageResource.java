package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExamineeRepository;
import at.htl.control.ScreenshotRepository;
import at.htl.entity.Exam;
import at.htl.entity.Examinee;
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
    public Multi<Response> upload(InputStream is, @QueryParam("filename") String filename) throws IOException {

        if (filename.isBlank()) {
            filename = "unknown.xxx";
            LOG.error("filename is empty");
        }

        LOG.info("Trying to save the file");
        try (is) {
            String[] fullPath = filename.split("_|\\.");

            File dir = new File(pathOfScreenshots);
            dir.mkdir();
            dir = new File(pathOfScreenshots+"/exam/"+fullPath[1]+"/"+fullPath[2]+"_"+fullPath[3]);
            dir.mkdir();

            Files.copy(
                    is,
                    Paths.get(dir.getPath(), filename),
                    StandardCopyOption.REPLACE_EXISTING
            );
            LOG.info("saved file");
            cnt++;
            //LOG.info(filename);
/*            String[] path = filename.split("_");
            LOG.info(path[1]);
            Exam exam = examRepository.findById(Long.valueOf(path[1]));
            Examinee examinee = examineeRepository.findByName(exam.id, path[2], path[3]);
            ScreenshotDto screenshotDto = new ScreenshotDto(cnt, exam, examinee, "");
            screenshotRepository.postScreenshot(screenshotDto);*/
        }catch (NullPointerException ignore) {
            LOG.error("Error while saving the file");
        }

        return Multi.createFrom().items(Response.ok().build());
    }
}