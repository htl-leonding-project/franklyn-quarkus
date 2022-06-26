package at.htl.control;

import at.htl.entity.SchoolClass;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SchoolClassRepository implements PanacheRepository<SchoolClass> {
    public List<SchoolClass> getAllSchoolClasses() {
        return findAll().list();
    }

    public List<SchoolClass> getCurrentSchoolClass() {

        //TODO: add WebUntis connection to get current year

        return find("year = ?1", "2022").list();
    }

    public List<SchoolClass> getStats() {

        //TODO: add WebUntis connection to get current year

        return find("#SchoolClass.getStats", "2022")
                .list();
    }

    public SchoolClass getSchoolClassByTitleAndYear(String title, String year) {
        return find("#SchoolClass.getByTitleAndYear", title, year).singleResult();
    }
}
