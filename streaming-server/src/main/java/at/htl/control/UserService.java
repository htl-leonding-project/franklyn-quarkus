package at.htl.control;

import at.htl.util.UtilClass;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class UserService {

    @ConfigProperty(name = "exam-directory")
    String examDirectory;

    public boolean initializeUser(String firstname, String lastname, String examTitle) {

        File exam = UtilClass.folderNameForTitleDateAndDirectory(examTitle,LocalDate.now(),examDirectory);
            File newUser = new File(String.format("%s/%s%s",
                    exam,
                    firstname,
                    lastname
            ));
            newUser.mkdir();
            File alpha = new File(newUser.getPath()+"/alpha");
            File beta = new File(newUser.getPath()+"/beta");
        return alpha.mkdir() && beta.mkdir();
    }
}
