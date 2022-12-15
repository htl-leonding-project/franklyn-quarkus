package at.htl.control;

import at.htl.boundary.ExamineeService;
import at.htl.boundary.ImageService;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

@ApplicationScoped
public class ApiCalls {


    @Inject
    @RestClient
    ImageService imageService;
    @Inject
    @RestClient
    ExamineeService examineeService;
    private String firstName = "";
    private String lastName = "";
    private Long id;
    private Long cnt = 1L;
    private Long examineeId =1L;
    Scanner sc = new Scanner(System.in);

    @Scheduled(every = "5s")
    public void sendScreenshots(){
        if (!firstName.equals("") && !lastName.equals("")) {
            try {
                Robot robot = new Robot();
                String fileExt = "png";
                String fileName = cnt+"_"+lastName+"_"+firstName+"." + fileExt;
                cnt++;

                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                File newFile = new File(fileName);
                ImageIO.write(screenFullImage, fileExt, newFile);

                Log.info(imageService.uploadFile(newFile, fileName).getEntity());

                //System.out.println(imageService.uploadFile(newFile, fileName, examineeId).getEntity());
                Log.info("A full screenshot saved!");
                //System.out.println("A full screenshot saved!");
            } catch (AWTException | IOException ex) {
                System.err.println(ex);
            }
        }
    }

    public void enterName(String id) throws URISyntaxException, IOException {

        String responseString = "";
        String firstName = "";
        String lastName = "";

        do {
            System.out.print("Enter your first name: ");
            firstName = sc.next();

            System.out.print("Enter your last name: ");
            lastName = sc.next();

            Response response = examineeService.enrollStudentForExam(Long.parseLong(id), firstName, lastName);

            Object entity = response.getEntity();
            responseString = entity.toString();
            if (responseString.equals("-1")) {
                System.out.println("You are already enrolled for this exam!");
            }
        } while (responseString.equals("-1"));
    }

    public void enterPIN() throws URISyntaxException, IOException {
        String responseString = "";

        do {
            System.out.print("Enter your pin: ");
            String pin = sc.next();

            Response response = examineeService.verifyPIN(pin);
            Object entity = response.getEntity();
            responseString = entity.toString();
        } while (responseString.equals("0"));

        enterName(responseString);
    }
}
