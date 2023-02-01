package at.htl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main{

    public static void main(String[] args) throws IOException, AWTException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Vorname: ");
        String firstName = sc.nextLine();

        System.out.print("Nachname: ");
        String lastName = sc.nextLine();

        Robot robot = new Robot();
        String format = "png";
        String localDateTime = LocalDateTime.now().toString().replace(':', '_');
        String fileName = localDateTime+"_"+lastName+"_"+firstName+"." + format;

        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
        File newFile = new File(fileName);
        ImageIO.write(screenFullImage, format, newFile);

        String charset = "UTF-8";
        String requestURL = "http://localhost:8080/";

        try {
            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField("description", "Cool Pictures");
            multipart.addFormField("keywords", "Java,upload,Spring");

            multipart.addFilePart("fileUpload", newFile);

            List<String> response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}