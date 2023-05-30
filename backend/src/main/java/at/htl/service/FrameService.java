package at.htl.service;


import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

@ApplicationScoped
public class FrameService {
    private static final String ALPHA_FRAMES_DIR = "alpha-frames/";
    private static final String FINAL_FRAMES_DIR = "images/";
    private static String alphaFramePath;


    private static void createDirectory(String name) {
        File dir = new File(name);
        dir.mkdirs();
    }

    public static boolean saveAlphaFrame(byte[] imageData) {

        try {
            createDirectory(ALPHA_FRAMES_DIR);
            String fileName = generateFileName();
            String filePath = ALPHA_FRAMES_DIR + fileName;
            alphaFramePath = filePath;

            File imageFile = new File(filePath);
            // Save image
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageData);
            fos.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private static String generateFileName() {
        long timestamp = System.currentTimeMillis();
        return "alpha-frame_" + timestamp + ".png";
    }


    public void mergeWithAlphaFrame(byte[] file) {
        createDirectory(FINAL_FRAMES_DIR);
        OpenCV.loadLocally();
        var alphaFrame = Imgcodecs.imread(alphaFramePath);
        var betaFrame = Imgcodecs.imdecode(new MatOfByte(file), Imgcodecs.IMREAD_COLOR);
        System.out.println(alphaFrame.type() + " " + alphaFrame.size());
        System.out.println(betaFrame.type() + " " + betaFrame.size());
        Imgproc.resize(alphaFrame, alphaFrame, new Size(1280, 720));


        var diffMask = new Mat();
        Core.compare(betaFrame, new Scalar(0,0,0,0), diffMask, Core.CMP_NE);
        betaFrame.copyTo(alphaFrame, diffMask);


        var fileName = FINAL_FRAMES_DIR + "final-result-" + System.currentTimeMillis() + ".png";
        System.out.println(fileName);
        Imgcodecs.imwrite(fileName, alphaFrame);
    }


}
