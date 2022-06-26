package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExamineeRepository;
import at.htl.control.ScreenshotRepository;
import at.htl.entity.Examinee;
import at.htl.entity.Screenshot;
import at.htl.entity.dto.ScreenshotDto;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Path("upload")
public class ImageResource {
    @Inject
    Logger LOG;

    @Inject
    ScreenshotRepository screenshotRepository;

    @Inject
    ExamineeRepository examineeRepository;

    @Inject
    ExamRepository examRepository;

    Long cnt = 0L;

    @ConfigProperty(name = "PATHOFSCREENSHOT")
    String pathOfScreenshot;

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Screenshot upload(InputStream is, @QueryParam("filename") String filename) throws IOException {

        Screenshot returnSc = null;

        if (filename.isBlank()) {
            filename = "unknown.xxx";
            LOG.error("filename is empty");
        }

        LOG.info("Trying to save the file");
        try (is) {

            if(!Files.isDirectory(Paths.get(pathOfScreenshot))){
                new File(pathOfScreenshot).mkdir();
            }

            Files.copy(
                    is,
                    Paths.get(pathOfScreenshot, filename),
                    StandardCopyOption.REPLACE_EXISTING
            );

            Log.info("File: " + filename);

            String[] screenshot = filename
                    .split("\\.")[0]
                    .split("_");


            Log.info("persist screenshot: "+ filename);

            Examinee examinee;

            if(examineeRepository
                    .find("id = ?1",Long.valueOf(screenshot[0])) != null){

                examinee = examineeRepository.findById(Long.valueOf(screenshot[0]));

                Log.info("searched Examinee by id: " + Long.valueOf(examinee.id));

                ScreenshotDto sc = new ScreenshotDto(
                        Long.valueOf(screenshot[3]),
                        examinee.exam,
                        examinee,
                        filename
                );

                returnSc = screenshotRepository.postScreenshot(sc);

                Log.info("persistet Screenshot");

            }
        }
        return returnSc;
    }
}