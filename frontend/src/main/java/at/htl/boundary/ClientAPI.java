package at.htl.boundary;

import at.htl.control.InitBean;
import at.htl.entity.Examinee;
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
import java.time.LocalDate;

@Path("api/exams")
public class ClientAPI {
    String lastName;
    String firstName;
    @Inject
    ClientResource clientResource;

    @Inject
    InitBean initBean;

    Examinee examinee;

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
        examinee = new Examinee(firstName, lastName);


        initBean.init(fName, lName);

        return Response.ok().build();

        //wenn pin korrekt --> user in db
        //wenn falsch --> Fehlermeldung
        /*initBean.init(firstName, lastName);
        return Response
                .ok().build();*/

    }

    @Path("enterPin")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public Response enterPIN(@Context UriInfo uriInfo
            , @FormParam("pin") String pin){

        //verify pin
        String date = String.valueOf(LocalDate.now());
        //Boolean verified = clientService.verifyPin(pin, date);
        /*if(verified){
            return Response.status(301)
                    .location(URI.create("api/exams/login"))
                    .build();
        }*/

        return Response.status(301)
                .location(URI.create("api/exams/enterPin"))
                .build();

        //if pin correct --> go to the countdown and start screenshotting
        //if pin not correct --> display error messag "pin is not correct"


        //user anlegen
        //weiterleiten um pin einzugeben
        //wenn pin korrekt --> user in db
        //wenn falsch --> Fehlermeldung
    }
}
