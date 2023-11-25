package at.htl.boundary;

import at.htl.control.ExamRepository;
import at.htl.entity.Exam;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/exam")
public class ExamResource {

    @Inject
    ExamRepository examRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exam> getAll() {
        return examRepository.listAll();
    }

}
