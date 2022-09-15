package at.htl.client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;
import java.util.TimerTask;

public class Screenshot extends TimerTask {

    private static String firstName = "Tamara";
    private static String lastName = "Melcher";

    private static HttpClient client;


    private static Long cnt = 1L;

    public Screenshot(String firstName, String lastName){

    }

    @Override
    public void run() {
        if (!Objects.equals(firstName, "") && !Objects.equals(lastName, "")) {
            try {
                Robot robot = new Robot();
                String format = "png";
                String fileName = cnt+"_"+lastName+"_"+firstName+"." + format;
                cnt++;

                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                File newFile = new File(fileName);
                ImageIO.write(screenFullImage, format, newFile);

                client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/upload?examineeId=1&filename=1_Melcher_Tamara.png"))
                        .timeout(Duration.ofMinutes(5))
                        .header("accept", "application/json")
                        .header("Content-Type", "application/octet-stream")
                        .POST(HttpRequest.BodyPublishers.ofFile(newFile.toPath()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(response);

/*                try {
                    File file = new File("src/data/a.txt");
                    HttpClient httpClient = HttpClient.newHttpClient();
                    HttpRequest httpRequest = HttpRequest.newBuilder()
                            .uri(new URI("http://localhost:8080/upload"))
                            .POST(HttpRequest.BodyPublishers.ofFile(newFile.toPath()))
                            .build();
                    //HttpResponse<String> httpResponse = httpClient.send(httpRequest, String.valueOf(HttpResponse.BodyHandler));
                    //System.out.println("Status Code: " + httpResponse.statusCode());
                    //System.out.println("Content: " + httpResponse.body());
                } catch (Exception e) {
                    System.err.println("Error: " + e.getMessage());
                }*/

            } catch (AWTException | IOException ex) {
                System.err.println(ex);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
