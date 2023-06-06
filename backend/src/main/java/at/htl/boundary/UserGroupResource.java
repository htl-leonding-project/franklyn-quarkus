package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.UserGroupRepository;
import at.htl.entity.Exam;
import at.htl.entity.UserGroup;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("usergroup")
public class UserGroupResource {


    @Inject
    UserGroupRepository userGroupRepository;

    @Inject
    ExamRepository examRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserGroup> listAll() {
        return userGroupRepository.getAllSchoolClasses();
    }

    /**
     * Gets schoolclass of the current year
     * @return schoolclass
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("current")
    public List<UserGroup> getCurrentSchoolClass() {
        return userGroupRepository.getCurrentSchoolClass();
    }

    /**
     * Counts how many exams all schoolclasses had
     * @return list of schoolclasses
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("stats")
    public List<UserGroup> getStats(){
        return userGroupRepository.getStats();
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
        return userGroupRepository.getSchoolClassByTitleAndYear(title, year);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public UserGroup getSchoolClassById(@PathParam("id") String id) {
        UserGroup userGroup = userGroupRepository.findById(Long.parseLong(id));
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
        userGroupRepository.getEntityManager().merge(userGroup);
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
