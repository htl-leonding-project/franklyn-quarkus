package at.htl.control;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import nu.pattern.OpenCV;

public class InitBean {

    void onStart(@Observes StartupEvent ev) {
        OpenCV.loadLocally();
    }

}
