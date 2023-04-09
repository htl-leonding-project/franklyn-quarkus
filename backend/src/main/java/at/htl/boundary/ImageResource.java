package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.ExamineeRepository;
import at.htl.control.ScreenshotRepository;
import at.htl.entity.*;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Path("upload") //TODO: CHANGE URL IN CLIENT (add /api)
public class ImageResource {
    @Inject
    Logger LOG;

    @Inject
    ScreenshotRepository screenshotRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    ExamineeRepository examineeRepository;

    @ConfigProperty(name = "PATHOFSCREENSHOT")
    String pathOfScreenshots;

    Long cnt = 0L;

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Multi<Response> download(InputStream is, @QueryParam("filename") String filename) throws IOException {

        if (filename.isBlank()) {
            filename = "unknown.xxx";
            LOG.error("filename is empty");
        }

        LOG.info("Trying to save the file");
        try (is) {
            String[] fullPath = filename.split("_|\\.");
            Exam exam = examRepository.findById(Long.valueOf(fullPath[3]));
            exam.examState = ExamState.RUNNING;
            exam.startTime = LocalDateTime.now();
            java.nio.file.Path path =
                    Paths.get(String.format("../../%s/%s_%s/%s_%s",
                            pathOfScreenshots,
                            exam.title,
                            exam.date,
                            fullPath[1],
                            fullPath[2]));
            Files.createDirectories(path);

            LOG.info(filename);
            Log.info(path.toString());

            Files.copy(
                    is,
                    Paths.get(path.toString(), filename),
                    StandardCopyOption.REPLACE_EXISTING
            );
            cnt++;
            LOG.info(filename);
            //String[] files = filename.split("_");
            //LOG.info(files[1]);
            //Exam exam = examRepository.findById(Long.valueOf(files[3]));
            examRepository.persist(exam);
            Examinee examinee = examineeRepository.findByName(exam.id, fullPath[1], fullPath[2]);
            examinee.isOnline = true;
            examinee.lastOnline = LocalDateTime.now();
            examineeRepository.persist(examinee);
            Screenshot screenshot = new Screenshot(
                    Timestamp.valueOf(LocalDateTime.now()),
                    cnt,
                    examinee,
                    pathOfScreenshots+"/"+ exam.title+"_"+ exam.date +"/"+fullPath[1]+"_"+fullPath[2]+"/"+filename,
                    exam
            );
            screenshotRepository.post(screenshot);
        }catch (NullPointerException ignore) {
            LOG.error("Error while saving the file");
        }

        return Multi.createFrom().items(Response.ok().build());
    }

    @POST
    @Path("video/{examId}/{examineeId}")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Response> generateVideoOfExamineeAndExamById(
            @PathParam("examId") String examId, @PathParam("examineeId") String examineeId){
        Log.info(examId);

        Exam exam = examRepository.findById(Long.parseLong(examId));
        Examinee examinee = examineeRepository.findById(Long.parseLong(examineeId));

        java.nio.file.Path screeshotPath =
                Paths.get(String.format("../../%s/%s_%s/%s_%s/",
                        pathOfScreenshots,
                        exam.title,
                        exam.date,
                        examinee.lastName,
                        examinee.firstName));

        try {
            FFmpeg ffmpeg = new FFmpeg("/usr/bin/ffmpeg");      //Path of ffmpeg installation(currently, ffmpeg on Linux default path)
            FFprobe ffprobe = new FFprobe("/usr/bin/ffmpeg");

            FFmpegBuilder builder = new FFmpegBuilder()
                    .addExtraArgs("-f", "image2")
                    .addExtraArgs("-pattern_type", "glob")
                    .addExtraArgs("-framerate", "1")
                    .setInput(screeshotPath + "/*.png")
                    .overrideOutputFiles(true)
                    .addOutput(String.format("%s/%s_%s_%s_video.mkv",
                            screeshotPath,
                            examinee.lastName,
                            examinee.firstName,
                            exam.title))
                    .addExtraArgs("-c:v", "libx264")
                    .addExtraArgs("-pix_fmt", "yuv420p")
                    .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

            executor.createJob(builder).run();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Multi.createFrom().items(Response.ok().build());
    }
}