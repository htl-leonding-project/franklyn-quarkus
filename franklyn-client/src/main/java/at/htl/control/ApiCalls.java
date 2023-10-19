package at.htl.control;

import nu.pattern.OpenCV;
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

import java.util.Scanner;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ApiCalls {

    //@Inject
    //@RestClient
    //ImageService imageService;

   /* @Inject
    @RestClient
    ExamineeService examineeService;*/
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
            LOG.info(alphaFramePath + " is the main frame");
            String fileExt = "png";
            String fileName = ++countOfImages + "_" + lastName + "_" + firstName + "_" + id + "." + fileExt;


            var newImage = getNewBufferedImage();


            File newFile = new File(pngFolder, fileName);
            LOG.info(newFile.getAbsoluteFile());
            ImageIO.write(newImage, fileExt, newFile);

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
        //frameService.saveAlphaFrame(fileToBytes);
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
                                    .withIntervalInSeconds(3)
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
