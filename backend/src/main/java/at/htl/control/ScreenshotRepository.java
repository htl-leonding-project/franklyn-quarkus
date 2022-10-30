package at.htl.control;

import at.htl.entity.Resolution;
import at.htl.entity.Screenshot;
import at.htl.entity.dto.ScreenshotDto;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
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
    public Screenshot findScreenshot(Long examineeId, Long screenshotNumber) {
        return find("#Screenshot.findScreenshot", examineeId, screenshotNumber)
                .firstResult();
    }

    /*
    * find the latest Screenshot
    * */
    public Screenshot findLatestScreenshot(Long examineeId) {
        return find("#Screenshot.findLatestScreenshot", examineeId)
                .firstResult();
    }

    /*
     * post a new screenshot
     */
    public Screenshot postScreenshot(ScreenshotDto screenshot){
        Screenshot s = new Screenshot(
                Timestamp.valueOf(LocalDateTime.now()),
                screenshot.runningNo(),
                screenshot.examinee(),
                Resolution.HD,
                1,
                pathOfScreenshot + "/exam/" + screenshot.exam().id + "/" + screenshot.examinee().id
        );
        this.persist(s);
        return s;
    }


}
