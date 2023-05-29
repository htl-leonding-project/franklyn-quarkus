package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.SchoolClassRepository;
import at.htl.entity.Exam;
import at.htl.entity.UserGroup;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("schoolclass")
public class SchoolClassResource {

    @Inject
    SchoolClassRepository schoolClassRepository;

    @Inject
    ExamRepository examRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserGroup> listAll() {
        return schoolClassRepository.getAllSchoolClasses();
    }

    /**
     * Gets schoolclass of the current year
     * @return schoolclass
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("current")
    public List<UserGroup> getCurrentSchoolClass() {
        return schoolClassRepository.getCurrentSchoolClass();
    }

    /**
     * Counts how many exams all schoolclasses had
     * @return list of schoolclasses
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stats")
    public List<UserGroup> getStats(){
        return schoolClassRepository.getStats();
    }

    /**
     * Looks for schoolclass by title and year
     * @return schoolclass
     */
    @GET
    @Path("{title}/{year}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserGroup getSchoolClassByTitleAndYear(
            @PathParam("title") String title,
            @PathParam("year") String year
    ){
        return schoolClassRepository.getSchoolClassByTitleAndYear(title, year);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserGroup getSchoolClassById(@PathParam("id") String id) {
        UserGroup userGroup = schoolClassRepository.findById(Long.parseLong(id));
        if(userGroup == null)
            return null;
        return userGroup;
    }

    /**
     * Posts new schoolclass
     * @return schoolclass
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserGroup addSchoolClass(UserGroup userGroup){
        schoolClassRepository.getEntityManager().merge(userGroup);
        return userGroup;
    }


    @GET
    @Path("exam/id/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<UserGroup> getExaminerByExamId(@PathParam("id") String id) {
        Exam exam = examRepository.findById(Long.parseLong(id));
        return exam.userGroups;
    }
}
