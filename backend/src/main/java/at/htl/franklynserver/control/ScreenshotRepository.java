package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Resolution;
import at.htl.franklynserver.entity.Screenshot;
import at.htl.franklynserver.entity.dto.ScreenshotDto;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Timestamp;

@ApplicationScoped
public class ScreenshotRepository implements PanacheRepository<Screenshot> {

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
/*    @ReactiveTransactional
    public void postScreenshot(ScreenshotDto screenshot){
        Screenshot s = new Screenshot(
                Timestamp.valueOf(screenshot.timestamp()),
                screenshot.runningNo(),
                screenshot.exam(),
                screenshot.examinee(),
                Resolution.HD,
                1
        );
        this.persist(s).subscribe().with(screenshot1 -> Log.info(screenshot.screenshotNumber));
    }*/


}
