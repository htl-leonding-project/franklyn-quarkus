package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ExamineeRepository;
import at.htl.franklynserver.entity.Examinee;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/examinees")
public class ExamineeResource {

    @Inject
    ExamineeRepository examineeRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Examinee>> getAll(){
        return examineeRepository.listAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Examinee> getExaminee(@PathParam("id") Long id) {
        return examineeRepository.findById(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Examinee> updateExaminee(@PathParam("id") Long id,Examinee examinee) {
        return Panache
                .withTransaction(() -> examineeRepository.findById(id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.firstName = examinee.firstName;
                            entity.lastName = examinee.lastName;
                            return entity;
                        })
                        .onFailure().recoverWithNull());
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @ReactiveTransactional
    public Uni<Examinee> deleteExaminee(Examinee examinee) {
        Uni<Examinee> examineeUni = examineeRepository.findById(examinee.id);
        examineeRepository.deleteById(examinee.id)
                .subscribe().with(e ->{Log.info(examinee.lastName);});
        return examineeUni;
    }

}
