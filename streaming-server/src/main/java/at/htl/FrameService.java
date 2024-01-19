package at.htl;


import at.htl.control.ImageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.nio.file.Files;
import java.nio.file.Path;

@ApplicationScoped
public class FrameService {

    @Inject
    ImageService imageService;

    @Inject
    Logger logger;


    private String generateFileName() {
        long timestamp = System.currentTimeMillis();
        return "alpha-frame_" + timestamp + ".png";
    }


    public byte[] generateStreamingFrame(String testName, String studentName) {
        var pathOfAlphaFrame = Path.of(imageService.getThePathOfLatestAlphaFrame(studentName, testName));
        var pathOfBetaFrame = Path.of(imageService.getThePathOfLatestBeta(studentName, testName));
        logger.info(pathOfAlphaFrame.toString());
        logger.info(pathOfBetaFrame);
        var alphaFileExists = Files.exists(pathOfAlphaFrame);
        var betaFrameExists = Files.exists(pathOfBetaFrame);
        logger.log(Logger.Level.INFO, alphaFileExists + " is the status of alphaframe");
        logger.log(Logger.Level.INFO, betaFrameExists + " is the status of betaframe");

        var alphaFrame = Imgcodecs.imread(pathOfAlphaFrame.toString(), Imgcodecs.IMWRITE_PAM_FORMAT_RGB_ALPHA);
        var betaFrame = Imgcodecs.imread(pathOfBetaFrame.toString(), Imgcodecs.IMWRITE_PAM_FORMAT_RGB_ALPHA);

        System.out.println(alphaFrame.type() + " " + alphaFrame.size());
        System.out.println(betaFrame.type() + " " + betaFrame.size());


        var diffMask = new Mat();
        Core.compare(betaFrame, new Scalar(0, 0, 0, 0), diffMask, Core.CMP_NE);
        betaFrame.copyTo(alphaFrame, diffMask);
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", alphaFrame, matOfByte);
        logger.log(Logger.Level.INFO, matOfByte.toArray().length);
        return matOfByte.toArray();
    }
}