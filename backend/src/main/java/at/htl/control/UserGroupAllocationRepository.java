package at.htl.control;

import at.htl.entity.UserGroupAllocation;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserGroupAllocationRepository implements PanacheRepository<UserGroupAllocation> {
}
