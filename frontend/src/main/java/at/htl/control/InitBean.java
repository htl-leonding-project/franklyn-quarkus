package at.htl.control;

import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

public class InitBean {
    void init(@Observes StartupEvent event) {
        int count = 0;
        while (count <= 10) {
            count++;
            try {
                Thread.sleep(5000);
                Robot robot = new Robot();
                String format = "jpg";
                String fileName = "FullScreenshot"+count+"." + format;

                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                ImageIO.write(screenFullImage, format, new File(fileName));

                System.out.println("A full screenshot saved!");
            } catch (AWTException | IOException | InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
}
