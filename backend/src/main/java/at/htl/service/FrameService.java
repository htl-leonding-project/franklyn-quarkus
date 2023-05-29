package at.htl.service;


import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@ApplicationScoped
public class FrameService {
    private static final String ALPHA_FRAMES_DIR = "alpha-frames/";

    public boolean saveAlphaFrame(byte[] imageData) {

        try {
            File alphaFramesDir = new File(ALPHA_FRAMES_DIR);
            alphaFramesDir.mkdirs();

            String fileName = generateFileName();
            String filePath = ALPHA_FRAMES_DIR + fileName;

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

    private String generateFileName() {
        long timestamp = System.currentTimeMillis();
        return "alpha-frame_" + timestamp + ".png";
    }
}
