package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Screenshot;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ScreenshotRepository implements PanacheRepository<Screenshot> {

    /*
     * find a screenshot by its number
     */
    public Uni<Screenshot> findScreenshot(int examineeId, int screenshotNumber) {
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
    /*@ReactiveTransactional
    public void postScreenshot(Screenshot screenshot){
        this.persist(screenshot).subscribe().with(screenshot1 -> Log.info(screenshot.screenshotNumber));
    }*/


}
