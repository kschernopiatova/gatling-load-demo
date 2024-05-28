package computerdatabase;

import computerdatabase.storeclasses.Scenarios;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class DemoStoreSimulation extends Simulation {

    private final HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://demostore.gatling.io");

    {
        setUp(Scenarios.defaultScenario.injectOpen(
                atOnceUsers(5),
                rampUsers(10).during(5),
                nothingFor(3),
                constantUsersPerSec(3).during(5)))
                .protocols(httpProtocol);
    }

//    {
//        setUp(scn.injectClosed(
//                constantConcurrentUsers(10).during(10),
//                rampConcurrentUsers(10).to(20).during(10),
//                constantConcurrentUsers(40).during(10))
//                .protocols(httpProtocol));
//    }
}
