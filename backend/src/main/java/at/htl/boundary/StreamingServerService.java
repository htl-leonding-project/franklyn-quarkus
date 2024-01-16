package at.htl.boundary;

import at.htl.entity.dto.ExamDto;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("")
@RegisterRestClient(configKey = "streaming-server")
public interface StreamingServerService {

    @POST
    @Path("exam/initialize")
    @Consumes(MediaType.APPLICATION_JSON)
    Response createExam(ExamDto examDto);

}
