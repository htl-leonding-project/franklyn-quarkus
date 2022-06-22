package at.htl.franklynserver.control;

import at.htl.franklynserver.entity.Examinee;
import at.htl.franklynserver.entity.Examiner;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.ext.web.Session;
import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.reactive.stage.Stage;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import static io.quarkus.hibernate.reactive.panache.PanacheEntity_.id;

public class InitBean {

    @Inject
    ExamineeRepository examineeRepository;

    @Inject
    ExaminerRepository examinerRepository;

    @ReactiveTransactional
    public void init(@Observes StartupEvent event){
        Examinee examinee = new Examinee("Jakob", "Unterberger");
        Examiner examiner = new Examiner("chahn", "Christine", "Hahn", false);
        examineeRepository.persist(examinee).subscribe().with(examinee1 -> Log.info(examinee1.firstName));
        //examinerRepository.persist(examiner).subscribe().with(examiner1 -> Log.info(examiner1.lastName));
        Panache.withTransaction(examiner::persist).subscribe().with(examiner1 -> Log.info(examiner1));
        if (examinerRepository.isPersistent(examiner)) {
            Log.info("Yes examiner");
        }

        if (examineeRepository.isPersistent(examinee)) {
            Log.info("Yes");
        }
    }
}
