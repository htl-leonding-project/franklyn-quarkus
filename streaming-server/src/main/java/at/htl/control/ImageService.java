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


    @Inject
    FrameService frameService;
    // Key username
    // Value latest alpha Frame
    ConcurrentHashMap<String, Integer> alphaFrames = new ConcurrentHashMap<>();

    // Key username
    // Value latest beta Frame
    ConcurrentHashMap<String, Integer> betaFrames = new ConcurrentHashMap<>();

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
        try {
            System.out.println(test);
            System.out.println(student);
            String fileExtension = ".png";
            String finalFileName;

            if (Objects.equals(type, "alpha")) {
                int count = alphaFrames.getOrDefault(student, 0) + 1;
                alphaFrames.put(student, count);
                betaFrames.put(student, 0);
                finalFileName = count + fileExtension;
            } else {
                int alphaCount = alphaFrames.getOrDefault(student, 1);
                int betaCount = betaFrames.getOrDefault(student, 0) + 1;
                betaFrames.put(student, betaCount);
                finalFileName = alphaCount + "-" + betaCount + fileExtension;
            }

            Path saveFilePath = Paths.get(examDirectory, test + "_" +
                            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
                    student.replace(" ", ""), type, finalFileName);

            File directory = saveFilePath.getParent().toFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            Files.write(saveFilePath, image);
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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


    public byte[] sendStreamImage(String test, String student) {
        var alphaFrameIndex = alphaFrames.get(student);
        String pathOfAlphaFrame = examDirectory + "/" + test + "/" + student + "/alpha/" + alphaFrameIndex + ".png";
        String pathOfBetaFrame = examDirectory + "/" + test + "/" + student + "/beta/" + alphaFrameIndex + "-" + betaFrames.get(student) + ".png";
        ;
        return frameService.generateStreamingFrame(pathOfAlphaFrame, pathOfBetaFrame);
    }


}