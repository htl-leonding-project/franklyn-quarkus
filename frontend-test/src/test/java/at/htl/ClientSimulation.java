package at.htl;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ClientSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("application/json")
            .userAgentHeader("Gatling/Performance Test");

    Iterator<Map<String, Object>> feeder =
            Stream.generate((Supplier<Map<String, Object>>) ()
                    -> Collections.singletonMap("username", UUID.randomUUID().toString())
            ).iterator();

    // Scenario
    ScenarioBuilder scn = CoreDsl.scenario("Load Test Creating Customers")
            .feed(feeder)
            .exec(http("create-customer-request")
                    .post("/upload")
                    .header("Content-Type", "application/json")
                    .body(StringBody("{ \"username\": \"${username}\" }"))
                    .check(status().is(201))
            )
            .exec(http("get-customer-request")
                    .get(session -> session.getString("location"))
                    .check(status().is(200))
            );

    public ClientSimulation() {
        setUp(
                scn.injectOpen(atOnceUsers(1))
            ).protocols(httpProtocol);
    }
}
