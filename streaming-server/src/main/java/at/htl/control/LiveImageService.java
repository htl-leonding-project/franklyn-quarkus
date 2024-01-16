package at.htl.control;

import at.htl.util.UtilClass;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.Session;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

@ApplicationScoped
public class LiveImageService {

    @ConfigProperty(name = "exam-directory")
    String directoryName;
    @Inject
    Logger logger;

    public void checkIfStudentOrTestDirectoryExist(String studentName, String testName, Session session, Map<String, Session> sessions
    ) {
        //TODO: Name of directory isn't just the exam title parse also the date for the Exam
        // should look something like this var testDirectory = UtilClass.folderNameForTitleDateAndDirectory(testName,date,directoryName);
        var testDirectory = Paths.get(directoryName + "/" + testName).toFile();
        var studentDirectory = Paths.get(testDirectory.toPath() + "/" + studentName).toFile();
        logger.log(Logger.Level.INFO, studentDirectory.toPath().toAbsolutePath());
        if (testDirectory.exists() && studentDirectory.exists()) {
            sessions.put(studentName, session);
            logger.log(Logger.Level.INFO, "Requesting images for student " + studentName);
        } else {
            try {
                logger.log(Logger.Level.ERROR, !testDirectory.exists() ? "Test doesn't exist" : "The student directory doesn't exist");
                session.close();
                throw new RuntimeException("test doesn't exist");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
