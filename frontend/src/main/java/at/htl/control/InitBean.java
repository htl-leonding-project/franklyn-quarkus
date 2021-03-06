package at.htl.control;

import at.htl.boundary.ClientAPI;
import at.htl.boundary.ImageService;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.scheduler.Scheduled;
import org.apache.james.mime4j.dom.datetime.DateTime;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// https://stackoverflow.com/questions/66347707/send-a-simple-post-request-from-quarkus-java

@ApplicationScoped
public class InitBean {

    @Inject
    @RestClient
    ImageService imageService;

    private String firstName = "";

    private String lastName = "";

    public void init( String firstName, String lastName) {

        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Scheduled(every = "5s")
    public void makeScreenshot(){
        if (firstName != "" && lastName != "") {
            try {
                Robot robot = new Robot();
                String format = "png";
                String localDateTime = LocalDateTime.now().toString().replace(':', '_');
                String fileName = localDateTime+"_"+lastName+"_"+firstName+"." + format;

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
}
