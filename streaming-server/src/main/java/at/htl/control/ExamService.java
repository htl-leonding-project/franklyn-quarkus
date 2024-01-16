package at.htl.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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

        String formatedTitle = title.toLowerCase().replace(" ","");
        String location = String.format("%s/%s_%s",
                examDirectory,
                formatedTitle,
                date.format(
                        DateTimeFormatter.ofPattern("yyyyMMdd")
                )
        );

        File newFolder = new File(location);
        if (newFolder.exists()) {
            purgeDirectory(newFolder);
            wasSuccessful = true;
        } else {
            wasSuccessful = newFolder.mkdirs();
        }
        return wasSuccessful;
    }
    private void purgeDirectory(File dir) {
        for (File file: Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
    }
}
