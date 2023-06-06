package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.UserRepository;
import at.htl.entity.Exam;
import at.htl.entity.User;
import io.smallrye.mutiny.Multi;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Path("download")
public class VideoResource {

    @ConfigProperty(name = "CURRENT_HOST")
    String currentHost;

    //@ConfigProperty(name = "FFMPEG_PATH")
    //String pathOfFFMPEG;

    @Inject
    Logger LOG;

    @Inject
    ExamRepository examRepository;

    @Inject
    UserRepository userRepository;
    @ConfigProperty(name = "PATHOFSCREENSHOT")
    String pathOfScreenshots;

    String videoName;

    java.nio.file.Path screenshotPath;

    Long cnt = 0L;

    private String generateVideoOfExamineeAndExamById(String examId, String examineeId) {
        Exam exam = examRepository.findById(Long.parseLong(examId));
        User user = userRepository.findById(Long.parseLong(examineeId));
        screenshotPath =
                Paths.get(String.format("../../%s/%s_%s/%s_%s/",
                        pathOfScreenshots,
                        exam.title,
                        exam.date,
                        user.lastName,
                        user.firstName));

        videoName = String.format("%s_%s_%s_video.mkv",
                user.lastName,
                user.firstName,
                exam.title);

        String videoOutputPath = String.format("%s/%s_%s/%s_%s/%s",
                pathOfScreenshots,
                exam.title,
                exam.date,
                user.lastName,
                user.firstName,
                videoName);

        try {
            FFmpeg ffmpeg = new FFmpeg("/opt/homebrew/bin/ffmpeg"); //pathOfFFMPEG     //Path of ffmpeg installation(currently, ffmpeg on Linux default path)
            FFprobe ffprobe = new FFprobe("/opt/homebrew/bin/ffmpeg");

            FFmpegBuilder builder = new FFmpegBuilder()
                    .addExtraArgs("-f", "image2")
                    .addExtraArgs("-pattern_type", "glob")
                    .addExtraArgs("-framerate", "1")
                    .setInput(screenshotPath + "/*.png")
                    .overrideOutputFiles(true)
                    .addOutput("../../" + videoOutputPath)
                    .addExtraArgs("-c:v", "libx264")
                    .addExtraArgs("-pix_fmt", "yuv420p")
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

            executor.createJob(builder).run();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return videoOutputPath;
    }


    @Path("video/{examId}/{examineeId}")
    @GET
    public Multi<String> getVideoByExamineeIdAndExamId(@PathParam("examineeId") String examineeId,
                                                       @PathParam("examId") String examId){
        String videoOutputPath = generateVideoOfExamineeAndExamById(examId, examineeId);
        Exam exam = examRepository.findById(Long.parseLong(examId));
        try {
            Files.copy(
                    new FileInputStream("../../"+videoOutputPath),
                    Paths.get(String.format("../../%s/%s_%s/", pathOfScreenshots, exam.title, exam.date), videoName),
                    StandardCopyOption.REPLACE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Multi.createFrom().items(currentHost+ videoOutputPath);
    }
}
