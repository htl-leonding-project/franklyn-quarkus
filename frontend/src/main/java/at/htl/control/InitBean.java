package at.htl.control;

import at.htl.boundary.ImageResource;
import at.htl.client.FileService;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@ApplicationScoped
public class InitBean {
    String DIRECTORYNAME = "./test-screenshots/";

    @Inject
    ImageResource resource;

    @Inject
    @RestClient
    FileService fileService;

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
            // https://stackoverflow.com/questions/66347707/send-a-simple-post-request-from-quarkus-java
            try {
                fileService.postFile(new FileOutputStream(file), file.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
