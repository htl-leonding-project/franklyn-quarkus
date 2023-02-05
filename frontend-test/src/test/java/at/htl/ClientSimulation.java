package at.htl;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.awt.*;
import java.io.*;
import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ClientSimulation extends Simulation {

    String fileName = "2023-02-01T09-11-28-793580100_Tran_Michael_3.png";
    byte[] postFile = new byte[(int) (new File(fileName)).length()];

    String firstName = "Michael";
    String lastName = "Tran";

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080/");

    // Scenario
    ScenarioBuilder scn = scenario("Load Test")
            .exec(
                    http("send screenshots request")
                            .post("upload")
                            .header("content-type", "application/octet-stream")
                            .body(InputStreamBody(session -> new ByteArrayInputStream(postFile)))
                            .check(status().is(200))
                            .queryParam("filename", fileName)
            );

    public ClientSimulation() throws IOException, AWTException {
        this.setUp(scn.injectOpen(constantUsersPerSec(50).during(Duration.ofSeconds(15))))
                .protocols(httpProtocol);
    }
}
