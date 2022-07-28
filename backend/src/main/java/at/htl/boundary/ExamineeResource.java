package at.htl.boundary;

import at.htl.control.ExamineeRepository;
import at.htl.entity.Examinee;
import io.quarkus.logging.Log;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("api/examinees")
public class ExamineeResource {

    @Inject
    ExamineeRepository examineeRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Examinee> getAll(){
        return examineeRepository.listAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Examinee getExaminee(@PathParam("id") Long id) {
        return examineeRepository.findById(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Examinee updateExaminee(@PathParam("id") Long id,Examinee examinee) {
        Examinee ex = examineeRepository.findById(id);
        if(ex != null){
            ex.firstName = examinee.firstName;
            ex.lastName = examinee.lastName;
            ex.exam = examinee.exam;
            ex.isOnline = examinee.isOnline;
            ex.lastOnline = examinee.lastOnline;
        }
        return ex;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examinee deleteExaminee(Examinee examinee) {
        Examinee examinee1 = examineeRepository.findById(examinee.id);
        if(examinee1 == null)
            return null;
        examineeRepository.deleteById(examinee.id);
        Log.info("Delete Examinee: " + examinee1);
        return examinee1;
    }

}
