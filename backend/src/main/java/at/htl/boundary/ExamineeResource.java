package at.htl.boundary;

import at.htl.control.ExamineeRepository;
import at.htl.entity.Examinee;
import at.htl.entity.dto.ExamineeDto;
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

    /**
     * @return list of examinees ordered by name
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Examinee> getAll(){
        return examineeRepository.listAll();
    }   //todo ordering by name

    /**
     * Get Examinee by Id
     * @return examinee (if found)
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Examinee getExaminee(@PathParam("id") Long id) {
        return examineeRepository.findById(id);
    }

    /**
     * Examinee will be updated
     * @return Updated examinee
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Examinee updateExaminee(@PathParam("id") Long id, ExamineeDto examinee) {
        Examinee ex = examineeRepository.findById(id);
        if(ex != null){
            ex.firstName = examinee.firstName();
            ex.lastName = examinee.lastName();
            ex.isOnline = examinee.isOnline();
        }
        return ex;
    }

    /**
     * Delete examinee by Id
     * @return screenshot of examinee
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Examinee deleteExaminee(Long id) {
        Examinee examinee1 = examineeRepository.findById(id);
        if(examinee1 == null)
            return null;
        examineeRepository.deleteById(id);
        return examinee1;
    }

}
