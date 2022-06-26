package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ExamRepository;
import at.htl.franklynserver.control.ExamineeRepository;
import at.htl.franklynserver.control.ScreenshotRepository;
import at.htl.franklynserver.entity.Examinee;
import at.htl.franklynserver.entity.IsCompressed;
import at.htl.franklynserver.entity.Resolution;
import at.htl.franklynserver.entity.Screenshot;
import at.htl.franklynserver.entity.dto.ScreenshotDto;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.subscription.Cancellable;
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
import java.time.Duration;
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

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    public Uni<Screenshot> upload(InputStream is, @QueryParam("filename") String filename) throws IOException {

        if (filename.isBlank()) {
            filename = "unknown.xxx";
            LOG.error("filename is empty");
        }

        LOG.info("Trying to save the file");
        try (is) {
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

            return screenshotRepository.persist(
                    new Screenshot(
                            Timestamp.valueOf(LocalDateTime.now()),
                            Long.valueOf(screenshot[3]),
                            Resolution.Native,
                            30,
                            IsCompressed.NOT_COMPRESSED,
                            pathOfScreenshot + "/" + filename
                    )
            );

//            if(examineeRepository
//                    .find("id = ?1",Long.valueOf(screenshot[0])) != null){
//
//                String finalFilename = filename;
//                Cancellable c = examineeRepository
//                        .find("id = ?1",Long.valueOf(screenshot[0]))
//                        .firstResult()
//                        .subscribe()
//                        .with(
//                                examinee -> {
//                                    Log.info("searched Examinee by id: " + Long.valueOf(examinee.id));
//
//                                    ScreenshotDto sc = new ScreenshotDto(
//                                            Long.valueOf(screenshot[3]),
//                                            examinee.exam,
//                                            examinee,
//                                            finalFilename
//                                    );
//
//                                    screenshotRepository.postScreenshot(sc);
//
//                                    Log.info("persistet Screenshot");
//                                },
//                                failure -> System.out.println("fail")
//                        );
//
//            }

        }
    }
}