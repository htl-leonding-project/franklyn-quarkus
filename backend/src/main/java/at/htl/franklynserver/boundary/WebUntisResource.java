package at.htl.franklynserver.boundary;

import io.quarkus.vertx.http.runtime.devmode.Json;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/webuntis")
public class WebUntisResource {

    @Inject
    @RestClient
    WebUntisService webUntisService;

    @Path("/authenticate")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(JsonObject body){

        System.out.println(body);
        return webUntisService.authenticate("htbla linz leonding", body);
    }

    @Path("/getteachers")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeacher(JsonObject body){

        System.out.println(body.toString());

        body.put("method", "authenticate");
        JsonObject param = new JsonObject()
                .put("user", "<<User>>")
                        .put("password", "<<Password>>")
                                .put("client", "CLIENT");
        body.put("params", param);
        webUntisService.authenticate("htbla linz leonding", body);

        System.out.println(body.toString());

        body.put("method", "getTeachers");
        body.put("params", "{}");

        return webUntisService.getTeachers("htbla linz leonding", body);
    }
}
