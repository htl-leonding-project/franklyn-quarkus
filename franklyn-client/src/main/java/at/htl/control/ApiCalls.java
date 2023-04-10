package at.htl.control;

import at.htl.boundary.ExamineeService;
import at.htl.boundary.ImageService;
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.imgscalr.Scalr;
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
    private String firstName = "";
    private String lastName = "";
    private Long examId = -1L;
    private Long examineeId = -1L;
    private String enrollOption = "";
    Scanner sc = new Scanner(System.in);

    private boolean authenticated = false;

    private int interval = 0;

    @Inject
    Scheduler scheduler;

    /***
     * send screenshot to backend
     */
    @Scheduled(every = "3s")                        // Scheduled time is useless due the fact that
    public void sendScreenshots() {                 //   it will get overwritten anyway
        if (authenticated) {
            try {
                Robot robot = new Robot();
                String fileExt = "png";
                String localDateTime = LocalDateTime.now().toString()
                        .replace(':', '-')
                        .replace(".", "-");
                String fileName = localDateTime + "_" + lastName + "_" + firstName + "_" + examId + "." + fileExt;

                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
                BufferedImage newImg = Scalr.resize(
                        screenFullImage,
                        1280,
                        720);
                File newFile = new File(fileName);
                ImageIO.write(newImg, fileExt, newFile);

                imageService.uploadFile(newFile, fileName);
                newFile.delete();
//                videoService.generateVideoOfExamineeAndExamById(examId.toString(), examineeId.toString());
            } catch (AWTException | IOException ex) {
                System.err.println(ex);

            }
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
                }
                else if(enrollOption.equalsIgnoreCase("N")){
                    response = -100L;
                }

            }
        } while (response == -1L);

        if(response != -100L){
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
        examineeId = examineeService
                .enrollStudentForExam(id, firstName, lastName);
        return examineeId;
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
        examineeId = examineeService
                .enrollStudentForExamAgain(id, firstName, lastName);
        return examineeId;
    }


    /***
     * send pin to backend (POST)
     * @return exam id
     */

    public Long enterPIN() {
        do {
            System.out.print("Enter your pin: ");
            String pin = sc.next();

            examId = examineeService.verifyPIN(pin);
        } while (examId == 0L);
        return examId;
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
                                    .withIntervalInSeconds(interval)
                                    .repeatForever())
                    .build();

            scheduler.scheduleJob(job, trigger);
            scheduler.pauseAll();
            scheduler.resumeJob(job.getKey());
        }
    }

    /***
     * Job executes sendScreenshot() method
     */

    public static class SendScreenshotJob implements org.quartz.Job {
        @Inject
        ApiCalls calls;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            calls.sendScreenshots();
        }
    }
}
