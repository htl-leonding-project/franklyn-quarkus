package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Resolution;
import at.htl.franklynserver.entity.Screenshot;
import at.htl.franklynserver.entity.dto.ScreenshotDto;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@ApplicationScoped
public class ScreenshotRepository implements PanacheRepository<Screenshot> {

    @ConfigProperty(name = "PATHOFSCREENSHOT")
    String pathOfScreenshot;

    /*
     * find a screenshot by its number
     */
    public Uni<Screenshot> findScreenshot(Long examineeId, Long screenshotNumber) {
        return find("#Screenshot.findScreenshot", examineeId, screenshotNumber)
                .firstResult();
    }

    /*
    * find the latest Screenshot
    * */
    public Uni<Screenshot> findLatestScreenshot(Long examineeId) {
        return find("#Screenshot.findLatestScreenshot", examineeId)
                .firstResult();
    }

    /*
     * post a new screenshot
     */
    public Uni<Screenshot> postScreenshot(ScreenshotDto screenshot){
        Screenshot s = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                screenshot.runningNo(),
                screenshot.exam(),
                screenshot.examinee(),
                Resolution.HD,
                1,
                pathOfScreenshot + "/" + screenshot.screenshotName()
        );
        return this.persist(s);
    }


}
