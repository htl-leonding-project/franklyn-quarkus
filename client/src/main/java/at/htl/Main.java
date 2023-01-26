package at.htl;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.HttpClients;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.*;

//https://stackoverflow.com/questions/3324717/sending-http-post-request-in-java

public class Main{

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your first name: ");
        String firstName = sc.next();

        System.out.print("Enter you last name: ");
        String lastName = sc.next();

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost();

        while(true) {
            try{
                Robot robot = new Robot();
                String format = "png";

                String localDateTime = LocalDateTime.now().toString()
                        .replace(':', '-')
                        .replace(".","-");
                String fileName = localDateTime+"_"+lastName+"_"+firstName+"." + format;

                System.out.println("send "+fileName);

                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                File newFile = new File(fileName);
                ImageIO.write(screenFullImage, format, newFile);

                httppost.setEntity(new FileEntity(newFile));
                //httppost.setURI(new URI("http://localhost:8080/api/upload?filename="+fileName));
                httppost.setURI(new URI("https://student.cloud.htl-leonding.ac.at/t.melcher/api/upload?filename="+fileName));
                httpclient.execute(httppost);

                TimeUnit.SECONDS.sleep(5);

            } catch (AWTException | IOException | InterruptedException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}