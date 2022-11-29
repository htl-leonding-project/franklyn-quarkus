package at.htl.franklynserver.control;

import at.htl.franklynserver.boundary.WebUntisService;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class WebUntisRepository {

    @Inject
    @RestClient
    WebUntisService webUntisService;

    public String getAllTeachers(){
        authenticate("if160180","houun432");

        String body = new JsonObject()
                .put("id", "anyID")
                .put("method", "getTeachers")
                .put("params", "{}")
                .put("jsonrpc", "2.0").toString();

        String res = webUntisService.getAllTeachers(body).toString();

        logout();
        return res;
    }

    public void authenticate(String user, String pwd){
        String param = String.format("\"user\": \"{0}\", \"password\":\"{1}\", \"client\":\"CLIENT\"}", user, pwd);

        String body = new JsonObject()
                .put("id", "anyID")
                .put("method", "authenticate")
                .put("params", param)
                .put("jsonrpc", "2.0").toString();

        webUntisService.login(body);
    }

    public void logout(){
        String body = new JsonObject()
                .put("id", "anyID")
                .put("method", "logout")
                .put("params", "{}")
                .put("jsonrpc", "2.0").toString();

        webUntisService.logout(body);
    }
}
