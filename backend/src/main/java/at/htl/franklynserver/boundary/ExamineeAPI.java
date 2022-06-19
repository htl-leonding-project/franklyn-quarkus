package at.htl.franklynserver.boundary;

import at.htl.franklynserver.control.ExamineeRepository;
import at.htl.franklynserver.entity.Exam;
import at.htl.franklynserver.entity.Examinee;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("api/examinees")
public class ExamineeAPI {

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
    @Consumes(MediaType.MULTIPART_FORM_DATA)
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
    public Uni<Examinee> deleteExaminee(Examinee examinee) {
        Uni<Examinee> examineeUni = examineeRepository.findById(examinee.id);
        if (examineeUni != null) {
            examineeRepository.delete(examinee);
        }
        return examineeUni;
    }

}
