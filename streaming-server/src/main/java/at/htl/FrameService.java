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


    public byte[] generateStreamingFrame(String pathOfAlphaFrame, String pathOfBetaFrame) {


        var alphaFrame = Imgcodecs.imread(pathOfAlphaFrame, Imgcodecs.IMWRITE_PAM_FORMAT_RGB_ALPHA);
        var betaFrame = Imgcodecs.imread(pathOfBetaFrame, Imgcodecs.IMWRITE_PAM_FORMAT_RGB_ALPHA);

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