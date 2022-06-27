package at.htl.boundary;

import at.htl.control.InitBean;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("api/client")
public class ClientAPI {
    String lastName;
    String firstName;

    Long examId;
    @Inject
    ClientResource clientResource;

    @Inject
    @RestClient
    ExamineeService examineeService;

    @Inject
    InitBean initBean;

    @Path("login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response login(@Context UriInfo uriInfo
            , @FormParam("fname") String fName
            , @FormParam("lname") String lName){
        this.firstName = fName;
        this.lastName = lName;

        Long examineeId = examineeService.enrollStudentForExam(examId, firstName, lastName);

        initBean.init(firstName, lastName, examineeId);
        return Response
                .ok().build();

        //user anlegen
        //weiterleiten um pin einzugeben
        //wenn pin korrekt --> user in db
        //wenn falsch --> Fehlermeldung

    }

    @Path("pin")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response enterPIN(@Context UriInfo uriInfo
            , @FormParam("pin") String pin){

        //verify pin

        //if pin correct --> go to the countdown and start screenshotting
        //if pin not correct --> display error messag "pin is not correct"
        examId = examineeService.verifyPIN(pin);
        if(examId == 0){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(clientResource.enterPin())
                    .type(MediaType.TEXT_HTML_TYPE)
                    .build();
        }

        Log.info(examId);

        return Response
                .status(Response.Status.ACCEPTED)
                .entity(clientResource.login())
                .type(MediaType.TEXT_HTML_TYPE)
                .build();


        //user anlegen
        //weiterleiten um pin einzugeben
        //wenn pin korrekt --> user in db
        //wenn falsch --> Fehlermeldung
    }
}
