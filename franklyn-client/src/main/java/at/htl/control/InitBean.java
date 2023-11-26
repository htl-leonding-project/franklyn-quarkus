package at.htl.control;


import at.htl.boundary.UserService;
import io.quarkus.runtime.ShutdownContext;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import nu.pattern.OpenCV;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.File;
import java.util.logging.Logger;

@ApplicationScoped
public class InitBean {

    private static final Logger LOGGER = Logger.getLogger("ListenerBean");

    File jpgFolder = new File("jpgImages/");
    File pngFolder = new File("pngImages/");

    void onStart(@Observes StartupEvent event) {
        LOGGER.info("Application started");
        OpenCV.loadLocally();


        if (!pngFolder.exists()) {
            pngFolder.mkdir();
        }
        if (!jpgFolder.exists()) {
            jpgFolder.mkdir();
        }

    }

    void onStop(@Observes ShutdownEvent event) {
        //TODO: Delete folders if you have permissions
        jpgFolder.delete();
        pngFolder.delete();
        if (jpgFolder.exists() || pngFolder.exists()) {
            LOGGER.warning("Failed to delete the folders");
        } else  {
            LOGGER.info("Folders were deleted successfully");
        }
        LOGGER.info("Application stopped");


    }
}
