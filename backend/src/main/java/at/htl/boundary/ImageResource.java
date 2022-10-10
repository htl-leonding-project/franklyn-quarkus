package at.htl.franklynserver.boundary;

import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Path("upload")
public class ImageResource {
    @Inject
    Logger LOG;

    @ConfigProperty(name = "PATHOFSCREENSHOT")
    String pathOfScreenshots;

    @POST
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Response> upload(InputStream is, @QueryParam("filename") String filename) throws IOException {

        if (filename.isBlank()) {
            filename = "unknown.xxx";
            LOG.error("filename is empty");
        }

        LOG.info("Trying to save the file");
        try (is) {
            String[] fullPath = filename.split("_|\\.");

            File dir = new File(pathOfScreenshots);
            dir.mkdir();
            dir = new File(pathOfScreenshots+"/"+fullPath[1]+"_"+fullPath[2]);
            dir.mkdir();

            Files.copy(
                    is,
                    Paths.get(dir.getPath(), filename),
                    StandardCopyOption.REPLACE_EXISTING
            );
            LOG.info("saved file");
        }

        return null;
    }
}