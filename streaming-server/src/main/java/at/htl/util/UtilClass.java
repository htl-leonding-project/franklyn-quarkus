package at.htl.util;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class UtilClass {

    public static String formatTitle(String title) {
        return title.toLowerCase().replace(" ","");
    }

    static public File folderNameForTitleDateAndDirectory(String title, LocalDate date, String examDirectory) {
        return new File(String.format("%s/%s_%s",
                examDirectory,
                formatTitle(title),
                date.format(
                        DateTimeFormatter.ofPattern("yyyyMMdd")
                )
        ));
    }
    static public void purgeDirectory(File dir) {
        for (File file: Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
        }
    }
}
