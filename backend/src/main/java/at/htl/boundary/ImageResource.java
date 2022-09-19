package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExamineeRepository;
import at.htl.control.ScreenshotRepository;
import at.htl.entity.Examinee;
import at.htl.entity.Resolution;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    /**
     * Saves screenshot of desktop in filesystem (path in .env)
     * Saves path, runningno, examineeid of screenshot in db
     * @return screenshot of examinee
     */
    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Screenshot upload(InputStream is, @QueryParam("filename") String filename, @QueryParam("examineeId") Long examineeId) throws IOException {
        System.out.println(is.toString());
        System.out.println(is.readAllBytes().length);
        System.out.println(filename);
        System.out.println(examineeId);
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

            String[] splittedFileName = filename.split("_");

            String newFileName = String.valueOf(LocalDateTime.now()) + "_" + splittedFileName[1] + "_" + splittedFileName[2];
            Files.copy(
                    is,
                    Paths.get(pathOfScreenshot, newFileName),
                    StandardCopyOption.REPLACE_EXISTING
            );

            Log.info("File: " + filename);

            String[] screenshot = filename
                    .split("\\.")[0]
                    .split("_");

            Examinee examinee;

            if(true){

                examinee = examineeRepository.findById(examineeId);

                Log.info("searched Examinee by id: " + Long.valueOf(examinee.id));

/*                ScreenshotDto sc = new ScreenshotDto(
                        Long.valueOf(screenshot[3]),
                        examinee.exam,
                        examinee,
                        filename
                );*/

                String newPathOfScreenShot = pathOfScreenshot + "/"+ examinee.exam.id + "/" + examinee.lastName;
                LocalDateTime now = LocalDateTime.now();
                Timestamp timestamp = Timestamp.valueOf(now);
                Screenshot screenshot1 = new Screenshot(
                    timestamp,
                    Long.valueOf(splittedFileName[0]),
                    examinee,
                    Resolution.HD,
                    100,
                    pathOfScreenshot+ "/" + newFileName
                );

                screenshotRepository.persist(screenshot1);

/*                String newPathOfScreenShot = pathOfScreenshot + "/"+ examinee.exam.id + "/" + examinee.lastName;
                if(!Files.isDirectory(Paths.get(pathOfScreenshot))){
                    new File(pathOfScreenshot).mkdir();
                }

                Files.copy(
                        is,
                        Paths.get(pathOfScreenshot, newFileName),
                        StandardCopyOption.REPLACE_EXISTING
                );*/

                Log.info("persistet Screenshot");

            }
        }
        return returnSc;
    }
}