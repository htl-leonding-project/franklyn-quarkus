package at.htl.control;

import at.htl.boundary.ImageService;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

// https://stackoverflow.com/questions/66347707/send-a-simple-post-request-from-quarkus-java

@ApplicationScoped
public class InitBean {

    @Inject
    @RestClient
    ImageService imageService;

    private AtomicInteger count = new AtomicInteger();

    @Scheduled(every = "5s")
    void makeScreenshot() {

        count.incrementAndGet();
        try {
            Robot robot = new Robot();
            String format = "png";
            String fileName = "fullscreenshot-" + count.get() + "." + format;

            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            File newFile = new File(fileName);
            ImageIO.write(screenFullImage, format, newFile);

            System.out.println(imageService.uploadFile(newFile, fileName));

            System.out.println("A full screenshot saved!");
        } catch (AWTException | IOException ex) {
            System.err.println(ex);
        }
    }
}
