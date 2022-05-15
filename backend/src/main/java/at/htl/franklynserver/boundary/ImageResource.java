package at.htl.franklynserver.boundary;

import io.smallrye.mutiny.Multi;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Path("upload")
public class ImageResource {
    @Inject
    Logger LOG;

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
            Files.copy(
                    is,
                    Paths.get("file-upload", filename),
                    StandardCopyOption.REPLACE_EXISTING
            );
        }

        return null;
    }
}