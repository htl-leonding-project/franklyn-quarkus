package at.htl.control;

import at.htl.boundary.ExamineeService;
import at.htl.boundary.ImageService;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
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
    private Long id = -1L;
    private boolean authenticated = false;
    Scanner sc = new Scanner(System.in);

    @Scheduled(every = "5s")
    public void sendScreenshots() {
        if (authenticated) {
            try {
                Robot robot = new Robot();
                String fileExt = "png";
                String localDateTime = LocalDateTime.now().toString()
                        .replace(':', '-')
                        .replace(".", "-");
                String fileName = localDateTime + "_" + lastName + "_" + firstName + "_" + id  + "."+ fileExt;

                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                File newFile = new File(fileName);
                ImageIO.write(screenFullImage, fileExt, newFile);

                imageService.uploadFile(newFile, fileName);
                Log.info("A full screenshot saved!");

                if(newFile.delete()){
                    Log.info(String.format("Remove %s successfully", fileName));
                }
            } catch (AWTException | IOException ex) {
                System.err.println(ex);
            }
        }
    }

    public Long enterName(String id){
        Long response;

        do {
            System.out.print("Enter your first name: ");
            firstName = sc.next();

            System.out.print("Enter your last name: ");
            lastName = sc.next();

            response = executeService(id, firstName, lastName);

            if (response == -1L) {
                System.out.println("You are already enrolled for this exam!");
            }
        } while (response == -1L);
        authenticated = true;
        return response;
    }

    public Long executeService(String id, String firstName, String lastName) {
        return examineeService
                .enrollStudentForExam(id, firstName, lastName);
    }

    public Long enterPIN(){
        do {
            System.out.print("Enter your pin: ");
            String pin = sc.next();

            id = examineeService.verifyPIN(pin);
        } while (id == 0L);

        return id;
    }
}
