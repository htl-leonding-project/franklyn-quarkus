package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.SchoolClassRepository;
import at.htl.franklynserver.entity.SchoolClass;
import at.htl.franklynserver.entity.SchoolClassDTO;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("schoolclass")
public class SchoolClassResource {

    @Inject
    SchoolClassRepository schoolClassRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<SchoolClass>> getAllSchoolClasses() {
        return schoolClassRepository.getAllSchoolClasses();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("current")
    public Uni<List<SchoolClass>> getCurrentSchoolClass() {
        return schoolClassRepository.getCurrentSchoolClass();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stats")
    public Uni<List<SchoolClass>> getStats(){
        return schoolClassRepository.getStats();
    }

    @GET
    @Path("{title}/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SchoolClass> getSchoolClassByTitleAndYear(
            @PathParam("title") String title,
            @PathParam("year") String year
    ){
        return schoolClassRepository.getSchoolClassByTitleAndYear(title, year);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<SchoolClass> postSchoolClass(SchoolClass schoolClass){
        return schoolClassRepository.postSchoolClass(schoolClass);
    }

    @DELETE
    @Path("{id}")
    public Uni<Boolean> deleteSchoolClass(
            @PathParam("id") Long id
    ){
        return schoolClassRepository.deleteById(id);
    }
}
