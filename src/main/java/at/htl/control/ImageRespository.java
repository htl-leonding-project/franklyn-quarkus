package at.htl.control;

import at.htl.entity.Image;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ImageRespository implements PanacheRepository<Image> {
}
