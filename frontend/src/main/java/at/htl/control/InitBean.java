package at.htl.control;

import at.htl.boundary.ImageResource;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.Timer;

@ApplicationScoped
public class InitBean {
    String DIRECTORYNAME = "./test-screenshots/";

    @Inject
    ImageResource resource;

    void init(@Observes StartupEvent event) {
        int count = 0;
        File file = new File(DIRECTORYNAME);
        file.mkdir();
        while (count < 10) {
            count++;
            try {
                Robot robot = new Robot();
                String format = "jpg";
                String fileName = "FullScreenshot"+count+"." + format;

                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                File newFile = new File(DIRECTORYNAME+fileName);
                ImageIO.write(screenFullImage, format, newFile);

                resource.sentImage(Files.newInputStream(newFile.toPath()));

                System.out.println("A full screenshot saved!");
                Thread.sleep(5000);
            } catch (AWTException | IOException | InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
}
