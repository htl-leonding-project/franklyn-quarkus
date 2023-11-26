package at.htl.control;

import at.htl.entity.UserSession;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserSessionRepository implements PanacheRepository<UserSession> {
}
