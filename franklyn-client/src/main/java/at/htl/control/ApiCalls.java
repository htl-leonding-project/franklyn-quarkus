package at.htl.control;

import at.htl.boundary.StreamingServerService;
import at.htl.boundary.UserService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.imgscalr.Scalr;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoWriter;
import org.quartz.*;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.TriggerBuilder;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

import org.jboss.logging.Logger;

@ApplicationScoped
public class ApiCalls {

    @Inject
    @RestClient
    UserService userService;

    @Inject
    @RestClient
    StreamingServerService streamingServerService;


    private String firstName = "max";
    private String lastName = "muster";

    private String examTitle = "";
    private Long id = -1L;

    private static String alphaFramePath = "";
    private String enrollOption = "";
    Scanner sc = new Scanner(System.in);

    private boolean authenticated = false;

    private static int countOfImages = 0;

    private int interval = 12;

    private String fileExt = "png";

    private LocalDateTime endOfExam;


    @Inject
    Scheduler scheduler;

    @Inject
    Logger LOG;

    int imageWidth = 1280;
    int imageHeight = 720;
    int allowedDifferenceInPercentage = 30;

    /*@Inject
    @RestClient
    FrameService frameService;*/

    File jpgFolder = new File("jpgImages/");
    File pngFolder = new File("pngImages/");

    /***
     * send screenshot to backend
     */
    public void sendScreenshots() {

        try {
           // LOG.info(alphaFramePath + " is the alpha frame");
            String fileName = ++countOfImages + "_" + lastName + "_" + firstName + "_" + id + "." + fileExt;


            File newFile = new File(pngFolder, fileName);
            //LOG.info(newFile.getAbsoluteFile());
            ImageIO.write(getNewBufferedImage(), fileExt, newFile);

            // Konvertieren der PNG-Datei in JPG
            String jpgFileName = fileName.replace(".png", ".jpg");
            File jpgFile = new File(jpgFolder, jpgFileName);
            BufferedImage pngImage = ImageIO.read(newFile);
            ImageIO.write(pngImage, "jpg", jpgFile);

            // Alle JPGs zu einem MP4 konvertieren
            //mergeJpgImagesToVideo();

            if (alphaFramePath.isEmpty()) {
                updateAlphaFrame(newFile);
                return;
            }

           // LOG.info("Difference between main and " + countOfImages);

            var image1 = Imgcodecs.imread(alphaFramePath, Imgcodecs.IMREAD_UNCHANGED);
            var image2 = Imgcodecs.imread(newFile.getAbsolutePath(), Imgcodecs.IMREAD_UNCHANGED);

            Imgproc.cvtColor(image1, image1, Imgproc.COLOR_BGR2RGBA);
            Imgproc.cvtColor(image2, image2, Imgproc.COLOR_BGR2RGBA);

            Mat difference = new Mat();
            Core.compare(
                    convertColoredImagesToGray(image1),
                    convertColoredImagesToGray(image2),
                    difference,
                    Core.CMP_NE
            );

            if (difference.empty()) return;
            if (getDifferenceInPercentage(difference) >= allowedDifferenceInPercentage) {
                updateAlphaFrame(newFile);
            } else {
                saveBetaFrame(difference, image2);
            }


        } catch (
                Exception ex) {
            LOG.error(ex.getMessage());

        }

    }

    public BufferedImage getNewBufferedImage() {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            return Scalr.resize(
                    screenFullImage,
                    imageWidth,
                    imageHeight);
        } catch (Exception ex) {
        //    LOG.error(ex.getMessage());
        }
        return null;
    }


    private void saveBetaFrame(Mat difference, Mat screenShot) {
        var result = new Mat();
        screenShot.copyTo(result, difference);
        String pngDir = pngFolder.getPath() + File.separator + countOfImages +
                "_" + lastName + "_" + firstName + "_" + id + "." + fileExt;

        convertBlackPixelsToTransparentPixels(result);
        Imgcodecs.imwrite(pngDir, result);
        // Save file in Streaming-Server
        // streamingServerService.sendBetaFrame(fileToBytes,examTitle,lastName.toLowerCase()+firstName.toLowerCase());
    }

    private void mergeJpgImagesToVideo() {
        try {
            nu.pattern.OpenCV.loadLocally();

            Size frameSize = new Size(imageWidth, imageHeight);
            int fourCC = VideoWriter.fourcc('X', '2', '6', '4');
            VideoWriter videoWriter = new VideoWriter("video.mp4", fourCC, 30, frameSize);

            File[] jpgFiles = jpgFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
            if (jpgFiles != null) {
                for (File jpgFile : jpgFiles) {
                    Mat frame = Imgcodecs.imread(jpgFile.getAbsolutePath());
                    videoWriter.write(frame);
                }
            }

            videoWriter.release();

            LOG.info("Video created successfully at: video.mp4");
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }
    }

    private void updateAlphaFrame(File file) throws Exception {
        alphaFramePath = file.getAbsolutePath();
        var fileToBytes = Files.readAllBytes(Paths.get(alphaFramePath));
        streamingServerService.sendAlphaFrame(fileToBytes,examTitle,lastName.toLowerCase()+firstName.toLowerCase());
    }

    private Mat convertColoredImagesToGray(Mat coloredImage) {
        var grayDifference = new Mat();
        Imgproc.cvtColor(coloredImage, grayDifference, Imgproc.COLOR_RGB2GRAY);
        return grayDifference;
    }

    private double getDifferenceInPercentage(Mat grayDifference) {
        //var grayDifference = convertColoredImagesToGray(coloredDifferences);
        var totalPixels = grayDifference.cols() * grayDifference.rows();
        var nonZeroPixels = Core.countNonZero(grayDifference);

        var differenceInPercentage = (double) nonZeroPixels / totalPixels * 100;
       // LOG.info(differenceInPercentage);
        return differenceInPercentage;

    }

    private void convertBlackPixelsToTransparentPixels(Mat coloredDifferences) {
        var blackPixels = new Mat(coloredDifferences.size(), coloredDifferences.channels());
        Core.inRange(coloredDifferences, new Scalar(0, 0, 0, 255), new Scalar(0, 0, 0, 255), blackPixels);
        coloredDifferences.setTo(new Scalar(0, 0, 0, 0), blackPixels);
    }


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
        }
    }

    public Long enterPIN() {
        do {
            System.out.print("Enter your pin: ");
            String pin = sc.next();

            id = userService.verifyPIN(pin);
        } while (id == 0L);
        return id;
    }

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

    public void getIntervall(String examId) {
        interval = userService.getInterval(examId);
    }

    public Long executeEnrollService(String id, String firstName, String lastName) {
        return userService
                .enrollStudentForExam(id, firstName, lastName);
    }

    public Long executeEnrollAgainService(String id, String firstName, String lastName) {
        return userService
                .enrollStudentForExamAgain(id, firstName, lastName);
    }

    public void getEndOfExam(String examId) {
        endOfExam = userService.getEndOfExam(examId);
        Long hours =Duration.between(LocalDateTime.now(),endOfExam).toHours();
        Long minutes = Duration.between(LocalDateTime.now(), endOfExam).toMinutes();
        System.out.println("Time left -> Minuten: "+ minutes);
    }

    public void enrollInStreamingServer() {
        streamingServerService.enrollSelf(examTitle,firstName.toLowerCase(), lastName.toLowerCase());
    }

    public void getExamTitle(String examId) {
        examTitle = userService.getTitle(examId);
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
