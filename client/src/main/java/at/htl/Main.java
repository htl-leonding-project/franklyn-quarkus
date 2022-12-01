package at.htl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

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

    //http://localhost:8080/api/exams/enroll/1/Tamara/Melcher


    public static void enterName(String id) throws URISyntaxException, IOException {
        Scanner sc = new Scanner(System.in);

        String responseString = "";
        String firstName = "";
        String lastName = "";

        do {
            System.out.print("Enter your first name: ");
            firstName = sc.next();

            System.out.print("Enter your last name: ");
            lastName = sc.next();

            HttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet();
            httpGet.setURI(new URI("http://localhost:8080/api/exams/enroll/"+id+"/"+firstName+"/"+lastName));
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
            if(responseString.equals("-1")){
                System.out.println("You are already enroled for this exam!");
            }
        }while(responseString.equals("-1"));

        if (!responseString.equals("0")) {
            sendScreenshots(id, firstName, lastName);
        }
    }

    public static void main(String[] args) throws URISyntaxException, IOException {
        Scanner sc = new Scanner(System.in);

        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet();
        String responseString = "";

        do{
            System.out.print("Enter your pin: ");
            String pin = sc.next();

            httpGet.setURI(new URI("http://localhost:8080/api/exams/verifyPIN/"+pin));
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
        }while (responseString.equals("0"));
        enterName(responseString);
    }
    public static void sendScreenshots(String firstName, String lastName, String id) throws IOException, URISyntaxException {

        while(true) {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost();
            CloseableHttpResponse httpresponse = null;
            try{
                Robot robot = new Robot();
                String format = "png";

                String localDateTime = LocalDateTime.now().toString()
                        .replace(':', '-')
                        .replace(".","-");
                String fileName = localDateTime+"_"+id+"_"+lastName+"_"+firstName+"." + format;

                System.out.println("send "+fileName);

                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                File newFile = new File(fileName);
                ImageIO.write(screenFullImage, format, newFile);

                httppost.setEntity(new FileEntity(newFile));
                httppost.setURI(new URI("http://localhost:8080/upload?filename="+fileName));
                httpresponse= httpclient.execute(httppost);

                TimeUnit.SECONDS.sleep(5);

            } catch (AWTException | IOException | InterruptedException | URISyntaxException e) {
                throw new RuntimeException(e);
            }finally {
                assert httpresponse != null;
                httpresponse.close();
                httpclient.close();
            }
        }
    }
}