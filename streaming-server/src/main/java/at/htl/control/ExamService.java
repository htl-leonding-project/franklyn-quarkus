package at.htl.control;

import at.htl.util.UtilClass;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jdk.jshell.execution.Util;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

@ApplicationScoped
public class ExamService {

    @ConfigProperty(name = "exam-directory")
    String examDirectory;

    @Inject
    Logger LOG;

    public boolean initializeExam(String title, LocalDate date) {
        boolean wasSuccessful;

        File newFolder = UtilClass.folderNameForTitleDateAndDirectory(title,date,examDirectory);
        if (newFolder.exists()) {
            UtilClass.purgeDirectory(newFolder);
            wasSuccessful = true;
        } else {
            wasSuccessful = newFolder.mkdirs();
        }
        return wasSuccessful;
    }
}
