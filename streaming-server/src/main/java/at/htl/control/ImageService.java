package at.htl.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ImageService {

    @Inject
    Logger LOG;

    public boolean saveFrame(byte[] image) {
        LOG.info(image.length);
        return false;
    }

}
