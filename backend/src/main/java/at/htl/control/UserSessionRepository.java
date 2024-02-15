package at.htl.control;

import at.htl.entity.UserSession;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class UserSessionRepository implements PanacheRepository<UserSession> {

    @Inject
    Logger log;

    public boolean checkIfAlreadyPartOfExam(String firstName, String lastName, Long id) {
        boolean found = find("#UserSession.isUserPartOfExam", id, firstName, lastName).stream().findAny().isPresent();
        System.out.println(found);
        return found;
    }

    public Long getUserId(String firstName, String lastName, Long examId) {
        UserSession us = find("#UserSession.isUserAlreadyPartOfExam", examId, firstName, lastName).firstResult();
        if (us == null) {
            return 0L;
        } else {
            return us.getUser().getId();
        }

        //return us.getUser().getId();

    }

    public List<UserSession> getAllParticipantsOfExam(Long examId) {

        return list("where exam.id = ?1", examId).stream().toList();
    }

    public void kickUser(Long examId, Long userId){
       var session = find("#UserSession.getSessionByExamAndUserId", examId, userId).firstResult();
       log.info("SESSION:" + session);
       deleteById(session.getId());
    }


}
