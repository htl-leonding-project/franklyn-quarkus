package at.htl.control;

import at.htl.entity.UserGroup;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserGroupRepository implements PanacheRepository<UserGroup> {
    public List<UserGroup> getAllSchoolClasses() {
        return findAll().list();
    }

    public List<UserGroup> getCurrentSchoolClass() {

        //TODO: add WebUntis connection to get current year

        return find("year = ?1", "2022").list();
    }

    public List<UserGroup> getStats() {

        //TODO: add WebUntis connection to get current year

        return find("#SchoolClass.getStats", "2022")
                .list();
    }

    public UserGroup getSchoolClassByTitleAndYear(String title, String year) {
        return find("#SchoolClass.getByTitleAndYear", title, year).singleResult();
    }
}
