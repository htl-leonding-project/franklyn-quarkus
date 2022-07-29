package at.htl.boundary;

import at.htl.control.SchoolClassRepository;
import at.htl.entity.SchoolClass;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("schoolclass")
public class SchoolClassResource {

    @Inject
    SchoolClassRepository schoolClassRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SchoolClass> getAllSchoolClasses() {
        return schoolClassRepository.getAllSchoolClasses();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("current")
    public List<SchoolClass> getCurrentSchoolClass() {
        return schoolClassRepository.getCurrentSchoolClass();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stats")
    public List<SchoolClass> getStats(){
        return schoolClassRepository.getStats();
    }

    @GET
    @Path("{title}/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public SchoolClass getSchoolClassByTitleAndYear(
            @PathParam("title") String title,
            @PathParam("year") String year
    ){
        return schoolClassRepository.getSchoolClassByTitleAndYear(title, year);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SchoolClass postSchoolClass(SchoolClass schoolClass){
        schoolClassRepository.getEntityManager().merge(schoolClass);
        return schoolClass;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public SchoolClass deleteSchoolClass(
            @PathParam("id") Long id
    ){
        SchoolClass schoolClass = schoolClassRepository.findById(id);
        if(schoolClass == null)
            return null;
        schoolClassRepository.deleteById(id);
        return schoolClass;
    }
}
