package at.htl.client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Timer;

public class Init {

    private static String firstName = "Tamara";
    private static String lastName = "Melcher";

    private static Long cnt = 1L;
    private static Timer timer;


    public static void main(String  args[]) throws Exception{
        timer = new Timer();
        timer.schedule(new Screenshot(firstName, lastName), 0, 5 * 1000);
    }
}
