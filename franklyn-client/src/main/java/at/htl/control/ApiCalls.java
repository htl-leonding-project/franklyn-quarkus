package at.htl.control;

import at.htl.boundary.ExamineeService;
import at.htl.boundary.ImageService;
import io.quarkus.logging.Log;
import nu.pattern.OpenCV;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.imgscalr.Scalr;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.quartz.*;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;

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
    private String firstName = "max";
    private String lastName = "muster";
    private Long id = -1L;

    private static String mainFramePath = "";
    private String enrollOption = "";
    Scanner sc = new Scanner(System.in);

    private boolean authenticated = false;

    private static int countOfImages = 0;

    private int interval = 12;

    @Inject
    Scheduler scheduler;

    /***
     * send screenshot to backend
     */
    public void sendScreenshots() {

        try {
            OpenCV.loadLocally();
            Robot robot = new Robot();
            String fileExt = "png";
            String localDateTime = LocalDateTime.now().toString()
                    .replace(':', '-')
                    .replace(".", "-");
            String fileName = ++countOfImages + "_" + lastName + "_" + firstName + "_" + id + "." + fileExt;

            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            BufferedImage newImg = Scalr.resize(
                    screenFullImage,
                    1280,
                    720);
            File newFile = new File(fileName);
            System.out.println(newFile.getAbsoluteFile());
            ImageIO.write(newImg, fileExt, newFile);
            if (mainFramePath.length() == 0) {
                mainFramePath = newFile.getAbsolutePath();

            }
            if (countOfImages >= 2) {
                System.out.println("Difference between main and " + countOfImages);
                Mat image1 = Imgcodecs.imread(mainFramePath);
                Mat image2 = Imgcodecs.imread(newFile.getAbsolutePath());



                Mat difference = new Mat();
                Core.absdiff(image1, image2, difference);



                newFile.delete();
                String currentWorkingDir = System.getProperty("user.dir") + "/" + countOfImages +
                        "_" + lastName + "_" + firstName + "_" + id + "." + fileExt;
                Imgcodecs.imwrite(currentWorkingDir, difference);









            }
            //imageService.uploadFile(newFile, fileName);
            //newFile.delete();
        } catch (AWTException | IOException ex) {
            System.err.println(ex);

        }

    }

    /***
     * set enroll data
     * @param id
     * @return
     */

    public Long enterName(String id) {
        Long response;

        do {
            System.out.print("Enter your first name: ");
            firstName = sc.next();

            System.out.print("Enter your last name: ");
            lastName = sc.next();

            response = executeEnrollService(id, firstName, lastName);

            if (response == -1L) {
                System.out.println("You are already enrolled for this exam!");

                System.out.print("Enroll again with the same name? [Y | N]: ");
                enrollOption = sc.next();

                if (enrollOption.equalsIgnoreCase("Y")) {
                    response = executeEnrollAgainService(id, firstName, lastName);
                } else if (enrollOption.equalsIgnoreCase("N")) {
                    response = -100L;
                }

            }
        } while (response == -1L);

        if (response != -100L) {
            authenticated = true;
        }
        return response;
    }

    /***
     * enroll student in exam (POST)
     *
     * @param id
     * @param firstName
     * @param lastName
     * @return
     */

    public Long executeEnrollService(String id, String firstName, String lastName) {
        return examineeService
                .enrollStudentForExam(id, firstName, lastName);
    }

    /***
     * enroll student in exam again (POST)
     *
     * @param id
     * @param firstName
     * @param lastName
     * @return
     */
    public Long executeEnrollAgainService(String id, String firstName, String lastName) {
        return examineeService
                .enrollStudentForExamAgain(id, firstName, lastName);
    }


    /***
     * send pin to backend (POST)
     * @return exam id
     */

    public Long enterPIN() {
        do {
            System.out.print("Enter your pin: ");
            String pin = sc.next();

            id = examineeService.verifyPIN(pin);
        } while (id == 0L);
        return id;
    }

    /***
     * retrieve interval from backend (GET)
     * @param examId
     */

    public void getIntervall(String examId) {
        interval = examineeService.getInterval(examId);
    }

    /***
     * overwrite scheduler with interval given
     * @throws SchedulerException
     */

    public void setScheduler() throws SchedulerException {
        if (interval != 0) {

            JobDetail job = JobBuilder.newJob(SendScreenshotJob.class)
                    .withIdentity("scheduleJob", "grp")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("scheduleTrigger", "grp")
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(5)
                                    .repeatForever())
                    .build();

            scheduler.scheduleJob(job, trigger);
        }
    }

    /***
     * Job executes sendScreenshot() method
     */

    public static class SendScreenshotJob implements Job {
        @Inject
        ApiCalls calls;

        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            calls.sendScreenshots();
        }
    }
}
