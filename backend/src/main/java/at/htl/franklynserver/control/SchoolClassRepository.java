package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.SchoolClass;
import at.htl.franklynserver.entity.SchoolClassDTO;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SchoolClassRepository implements PanacheRepository<SchoolClass> {
    public Uni<List<SchoolClass>> getAllSchoolClasses() {
        return find("#SchoolClass.getAllSchoolClasses", SchoolClass.class).list();
    }

    public Uni<List<SchoolClass>> getCurrentSchoolClass() {

        //TODO: add WebUntis connection to get current year

        return find("#SchoolClass.getCurrentSchoolClass", 2022, SchoolClass.class).list();
    }

    public Uni<List<SchoolClass>> getStats() {

        //TODO: add WebUntis connection to get current year

        return find("#SchoolClass.getStats", 2022, SchoolClassDTO.class).list();
    }

    public Uni<SchoolClass> getSchoolClassByTitleAndYear(String title, String year) {
        return find("#SchoolClass.getByTitleAndYear", title, year, SchoolClass.class).firstResult();
    }

    public Uni<SchoolClass> postSchoolClass(SchoolClass schoolClass) {
        return persist(schoolClass);
    }
}
