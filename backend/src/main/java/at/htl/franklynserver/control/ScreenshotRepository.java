package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Screenshot;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
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
    public Uni<Screenshot> postScreenshot(Screenshot screenshot){
        return persist(screenshot);
    }


}
