package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Screenshot;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ScreenshotRepository implements PanacheRepository<Screenshot> {
}
