package at.htl.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.Session;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ImageService {

    @ConfigProperty(name = "exam-directory")
    String examDirectory;

    // Key username
    // Value latest alpha Frame
    ConcurrentHashMap<String, String> alphaFrames = new ConcurrentHashMap<>();

    // Key username
    // Value latest beta Frame
    ConcurrentHashMap<String, String> betaFrames = new ConcurrentHashMap<>();

    @Inject
    Logger logger;

    public void checkIfStudentOrTestDirectoryExist(String studentName, String testName) {
        //TODO: Name of directory isn't just the exam title parse also the date for the Exam
        // should look something like this var testDirectory = UtilClass.folderNameForTitleDateAndDirectory(testName,date,directoryName);
        var testDirectory = Paths.get(examDirectory + "/" + testName).toFile();
        var studentDirectory = Paths.get(testDirectory.toPath() + "/" + studentName).toFile();
        logger.log(Logger.Level.INFO, studentDirectory.toPath().toAbsolutePath());
        if (testDirectory.exists() && studentDirectory.exists()) {

            logger.log(Logger.Level.INFO, "Requesting images for student " + studentName);
        } else {
            logger.log(Logger.Level.ERROR, !testDirectory.exists() ? "Test doesn't exist" : "The student directory doesn't exist");
            throw new RuntimeException("test doesn't exist");


        }

    }

    public boolean saveFrame(byte[] image, String student, String test, String type) {
        System.out.println(test);
        System.out.println(student);
        String fileExtention = ".png";
        Long filename = System.currentTimeMillis();

        String finalFileName = String.format("%d%s", filename, fileExtention);

        Path saveFilePath = Path.of(String.format("%s/%s_%s/%s/%s/%s",
                examDirectory,
                test,
                LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                student.replace(" ", ""),
                type, finalFileName)
        );

        if (Objects.equals(type, "alpha")) {
            alphaFrames.put(student, finalFileName);
        } else {
            betaFrames.put(student, finalFileName);
        }


        try {
            Files.write(saveFilePath, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }


    public String getThePathOfLatestAlphaFrame(String studentName, String testName) {
        return examDirectory +
                "/" +
                testName +
                "/" +
                studentName +
                "/" +
                "alpha" + "/" +
                alphaFrames.get(studentName);

    }

    public String getThePathOfLatestBeta(String studentName, String testName) {
        return examDirectory +
                "/" +
                testName +
                "/" +
                studentName +
                "/" +
                "beta" + "/" +
                betaFrames.get(studentName);

    }


}
