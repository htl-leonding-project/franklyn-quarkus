package at.htl;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ClientSimulation extends Simulation {

    ImageRepository imageRepository = new ImageRepository();
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080/")
            .userAgentHeader("Gatling/Performance Test");

    Iterator<Map<String, Object>> feeder =
            Stream.generate((Supplier<Map<String, Object>>) ()
                            -> {
                        try {
                            return Collections.singletonMap("filename", imageRepository.sendimage());
                        } catch (AWTException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            ).iterator();

    // Scenario
    ScenarioBuilder scn = CoreDsl.scenario("Load Test Creating Images")
            .feed(feeder)
            .exec(http("send-screenshots-request")
                    .post("upload")
                    .body(InputStreamBody(session -> new ByteArrayInputStream(new byte[] { 0, 1, 5, 4 })))
                    .queryParam("filename", "#{filename}")
            );

    public ClientSimulation() throws IOException, AWTException {
        this.setUp(scn.injectOpen(constantUsersPerSec(50).during(Duration.ofSeconds(15))))
                .protocols(httpProtocol);
    }
}
