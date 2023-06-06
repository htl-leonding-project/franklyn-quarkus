package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.control.UserRepository;
import at.htl.control.UserSessionRepository;
import at.htl.entity.Exam;
import at.htl.entity.User;
import at.htl.entity.UserSession;
import at.htl.entity.dto.ExamineeDto;
import at.htl.entity.dto.UserDto;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Path("usersession")
public class UserSessionResource {

    @Inject
    ExamRepository examRepository;

    @Inject
    UserSessionRepository userSessionRepository;

    @GET
    @Path("exam/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserDto> getExamineesByExamId(@PathParam("id") Long id) {
        Exam exam = examRepository.findById(id);
        if (exam == null)
            return null;
        List<UserSession> userSessionsList = userSessionRepository.listAllOrdered();
        List<UserDto> users = new LinkedList<>();
        UserDto tempExaminee;
        for (UserSession userSession : userSessionsList) {
            boolean isOnline = this.userSessionRepository.checkIfUserIsStillOnline(userSession.getUser(), exam.interval);
            if (Objects.equals(userSession.getExam(), id) && exam.adminId != userSession.getId()) {
                users.add(new UserDto(userSession.getUser().firstName, userSession.getUser().lastName, isOnline));
            }
        }
        return users;
    }

    @GET
    @Path("exam/{examId}/examiner/{examinerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<User> getExaminerByExamId(@PathParam("examId") Long examId, @PathParam("examinerId") Long examinerId) {
        Exam exam = examRepository.findById(examId);
        var examiners = exam.userExaminers;
        for (User examiner : examiners) {
            if (Objects.equals(examiner.id, examinerId)) {
                examiners.remove(examiner);
                break;
            }
        }
        return examiners;
    }
}
