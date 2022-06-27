package at.htl.boundary;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RequestScoped
@Path("api/client")
public class ClientResource {

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance login();
        public static native TemplateInstance enterPin();
    }

    @Path("pin")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance enterPin() {
        return Templates.enterPin();
    }

    @Path("login")
    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance login() {
        return Templates.login();
    }
}
