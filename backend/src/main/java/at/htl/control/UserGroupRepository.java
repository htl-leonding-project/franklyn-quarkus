package at.htl.control;

import at.htl.entity.UserGroup;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserGroupRepository implements PanacheRepository<UserGroup> {
}
