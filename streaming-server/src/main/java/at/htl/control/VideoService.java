package at.htl.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nu.pattern.OpenCV;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.video.Video;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@ApplicationScoped
public class VideoService {
    private final int imageWidth = 1080;
    private final int imageHeight = 720;

    @ConfigProperty(name = "exam-directory")
    String pathOfExamDirectory;

    @Inject
    Logger LOG;

    @Inject
    FrameService frameService;

    public void generateVideo(String testName, String studentName) {
        try {

            Size frameSize = new Size(imageWidth, imageHeight);
            int fourCC = VideoWriter.fourcc('X', 'V', 'I', 'D');

            var studentDirectory = new File(pathOfExamDirectory + "/" + testName + "/" + studentName);
            var pathOfVideo = "/" + studentDirectory + "/video/" + studentName + "_100.mp4";
            LOG.info(pathOfVideo);
            VideoWriter videoWriter = new VideoWriter(pathOfVideo, fourCC, 30, frameSize);
            var betaFrameFolder = new File(studentDirectory.getAbsolutePath() + "/beta");
            LOG.info(videoWriter.getBackendName());
            if (betaFrameFolder.exists() && betaFrameFolder.listFiles() != null) {
                //File[] jpgFiles = jpgFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
                Arrays.stream(Objects.requireNonNull(betaFrameFolder.listFiles())).toList().forEach(image -> {
                    var imageName = image.getName();
                    var alphaFrameName = imageName.substring(imageName.indexOf('-') + 1, imageName.indexOf('.'));
                    var mergedImage = frameService.generateStreamingFrame(
                            studentDirectory.getPath() + "/alpha/" + alphaFrameName + ".png", image.getPath());

                    var bytesToImage = new MatOfByte(mergedImage);
                    LOG.info(videoWriter.isOpened());

                    videoWriter.write(bytesToImage);
                });

                videoWriter.release();
                LOG.info("Video created successfully at: " + pathOfVideo);
            } else {
                throw new NullPointerException("Check the path of the alpha/beta frame file. It seems to not be correct");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
