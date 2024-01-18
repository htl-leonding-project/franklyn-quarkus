package at.htl.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ImageService {

    @ConfigProperty(name = "exam-directory")
    String examDirectory;

    // Key username
    // Value latest alpha Frame
    ConcurrentHashMap<String, String> alphaFrames;

    // Key username
    // Value latest beta Frame
    ConcurrentHashMap<String, String> betaFrames;

    @Inject
    Logger LOG;

    public boolean saveFrame(byte[] image, String student, String test, String type) {
        String fileExtention = Objects.equals(type, "alpha") ? ".jpg":".png";
        Long filename = System.currentTimeMillis();

        String finalFileName = String.format("%d%s", filename, fileExtention);

        Path saveFilePath = Path.of(String.format("%s/%s_%s/%s/%s/%s",
                examDirectory,
                test,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                student.replace(" ", ""),
                type, finalFileName)
        );
        System.out.println(saveFilePath);
        /*
        if (Objects.equals(type, "alpha")) {
            alphaFrames.put(student, finalFileName);
        } else {
            betaFrames.put(student, finalFileName);
        }

*/
        try {
            Files.write(saveFilePath,image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }



}
