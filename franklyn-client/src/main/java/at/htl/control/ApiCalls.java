package at.htl.control;

import at.htl.boundary.ExamineeService;
import at.htl.boundary.FrameService;
import at.htl.boundary.ImageService;
import nu.pattern.OpenCV;
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
import java.time.LocalDateTime;
import java.util.Scanner;
import org.jboss.logging.Logger;

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

    private static String alphaFramePath = "";
    private String enrollOption = "";
    Scanner sc = new Scanner(System.in);

    private boolean authenticated = false;

    private static int countOfImages = 0;

    private int interval = 12;

    @Inject
    Scheduler scheduler;

    @Inject
    Logger LOG;

    int imageWidth = 1280;
    int imageHeight = 720;
    int allowedDifferenceInPercentage = 30;

    @Inject
    @RestClient
    FrameService frameService;

    File jpgFolder = new File("jpgImages/");

    /***
     * send screenshot to backend
     */
    public void sendScreenshots() {

        try {
            LOG.info(alphaFramePath + " is the main frame");
            OpenCV.loadLocally();
            String fileExt = "png";
            String fileName = ++countOfImages + "_" + lastName + "_" + firstName + "_" + id + "." + fileExt;


            var newImage = getNewBufferedImage();

            File pngFolder = new File("pngImages/");
            if (!pngFolder.exists()) {
                pngFolder.mkdir();
            }

            File newFile = new File(pngFolder, fileName);
            LOG.info(newFile.getAbsoluteFile());
            ImageIO.write(newImage, fileExt, newFile);

            if (!jpgFolder.exists()) {
                jpgFolder.mkdir();
            }
            // Konvertieren der PNG-Datei in JPG
            String jpgFileName = fileName.replace(".png", ".jpg");
            File jpgFile = new File(jpgFolder, jpgFileName);
            BufferedImage pngImage = ImageIO.read(newFile);
            ImageIO.write(pngImage, "jpg", jpgFile);

            // Alle JPGs zu einem MP4 konvertieren
            mergeJpgImagesToVideo();

            if (alphaFramePath.length() == 0) {
                updateAlphaFrame(newFile);
                return;
            }
            if (countOfImages >= 2) {
                LOG.info("Difference between main and " + countOfImages);

                var image1 = Imgcodecs.imread(alphaFramePath);
                var image2 = Imgcodecs.imread(newFile.getAbsolutePath());

                var image1Gray = convertColoredImagesToGray(image1);
                var image2Gray = convertColoredImagesToGray(image2);

                Mat difference = new Mat();
                Core.compare(image1Gray, image2Gray, difference, Core.CMP_NE);


                if (!difference.empty()) {

                    var differenceInPercentage = getDifferenceInPercentage(difference);
                    if (differenceInPercentage >= allowedDifferenceInPercentage) {

                        updateAlphaFrame(newFile);

                    } else {

                        var result = new Mat();
                        image2.copyTo(result, difference);
                       // String currentWorkingDir = System.getProperty("user.dir") + "/" + countOfImages +
                       //         "_" + lastName + "_" + firstName + "_" + id + "." + fileExt;
                        String pngDir = pngFolder.getPath() + File.separator + countOfImages +
                                "_" + lastName + "_" + firstName + "_" + id + "." + fileExt;

                        result = convertBlackPixelsToTransparentPixels(result);

                        Imgcodecs.imwrite(pngDir, result);
                    }

                }

            }
            //imageService.uploadFile(newFile, fileName);
            //newFile.delete();
        } catch (Exception ex) {
            LOG.error(ex.getMessage());

        }

    }

    public BufferedImage getNewBufferedImage() {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            BufferedImage newImg = Scalr.resize(
                    screenFullImage,
                    imageWidth,
                    imageHeight);
            return newImg;
        }catch (Exception ex) {
                LOG.error(ex.getMessage());
        }
        return null;
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
        frameService.saveAlphaFrame(fileToBytes);
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
        LOG.info(differenceInPercentage);
        return differenceInPercentage;

    }

    private Mat convertBlackPixelsToTransparentPixels(Mat coloredDifferences) {
        var mask = Mat.zeros(coloredDifferences.size(), CvType.CV_8UC4);

        for (int row = 0; row < mask.rows(); row++) {
            for (int column = 0; column < mask.cols(); column++) {
                var pixelRGB = coloredDifferences.get(row, column);
                var red = pixelRGB[0];
                var green = pixelRGB[1];
                var blue = pixelRGB[2];

                double[] newPixelColors;
                if (green == 0 && red == 0 && blue == 0) {
                    newPixelColors = new double[]{0, 0, 0, 0};
                } else {
                    newPixelColors = new double[]{red, green, blue, 255};
                }
                mask.put(row, column, newPixelColors);
            }
        }
        return mask;
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
                                    .withIntervalInSeconds(1)
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
