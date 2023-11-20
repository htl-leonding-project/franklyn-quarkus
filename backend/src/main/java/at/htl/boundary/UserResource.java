package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.UserRepository;
import at.htl.control.UserSessionRepository;
import at.htl.entity.Exam;
import at.htl.entity.User;
import at.htl.entity.UserRole;
import at.htl.entity.dto.ExamineeDto;
import at.htl.entity.dto.ExaminerDto;
import at.htl.entity.dto.UserDto;
import io.quarkus.logging.Log;

import javax.ws.rs.Path;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Path("user")
public class UserResource {

    @Inject
    UserRepository userRepository;

    @Inject
    ExamRepository examRepository;

    @Inject
    UserSessionRepository userSessionRepository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listAll() {
        return userRepository.listAll();
    }


    @GET
    @Path("getById/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User getUserById(@PathParam("id") Long id) {
        return userRepository.findById(Long.parseLong(String.valueOf(id)));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User addUser(ExaminerDto examiner) {
        User e = new User(examiner.userName(), examiner.firstName(),true);
        userRepository.persist(e);
        Log.info("Saved Examiner: " + e.lastName);
        return e;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User deleteUser(Long id) {
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        userRepository.deleteById(id);
        return user;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public User updateUser(@PathParam("id") Long id, ExamineeDto examinee) {
        User user = userRepository.findById(id);
        if(user != null){
            user.firstName = examinee.firstName();
            user.lastName = examinee.lastName();
            user.isOnline = examinee.isOnline();
        }
        return user;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUserByRoleAndId(@PathParam("id") Long id) {
        return userSessionRepository.getUserByRoleAndId(UserRole.EXAMINEE, id);
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUserByRole(){
        return userSessionRepository.getUserByRole(UserRole.EXAMINEE);
    }   //todo ordering by name


}
