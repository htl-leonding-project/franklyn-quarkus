package at.htl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ImageRepository {

    public File sendimage() throws AWTException, IOException {
        String firstName = "Michael";
        String lastName = "Tran";

        Robot robot = new Robot();
        String format = "png";
        String localDateTime = LocalDateTime.now().toString()
                .replace(':', '_')
                .replace('.', '_');
        String fileName = localDateTime + "_" + lastName + "_" + firstName + "." + format;

        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
        File newFile = new File(fileName);
        ImageIO.write(screenFullImage, format, newFile);

        return newFile;
    }
}
