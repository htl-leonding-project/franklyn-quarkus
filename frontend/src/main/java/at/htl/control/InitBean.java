package at.htl.control;

import at.htl.boundary.ImageService;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// https://stackoverflow.com/questions/66347707/send-a-simple-post-request-from-quarkus-java

@ApplicationScoped
public class InitBean {

    @Inject
    @RestClient
    ImageService imageService;

    void init(@Observes StartupEvent event) {
        int count = 0;
        while (count < 10) {
            count++;
            try {
                Robot robot = new Robot();
                String format = "jpg";
                String fileName = "fullscreenshot-"+count+"." + format;

                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                File newFile = new File(fileName);
                ImageIO.write(screenFullImage, format, newFile);

                System.out.println(imageService.uploadFile(newFile, fileName));

                System.out.println("A full screenshot saved!");
                Thread.sleep(5000);
            } catch (AWTException | IOException | InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
}
