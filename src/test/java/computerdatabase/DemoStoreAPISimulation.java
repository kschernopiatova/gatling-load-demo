package computerdatabase;

import computerdatabase.apistoreclasses.Scenarios;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class DemoStoreAPISimulation extends Simulation {

    private static final HttpProtocolBuilder HTTP_PROTOCOL = http
            .baseUrl("https://demostore.gatling.io")
            .contentTypeHeader("application/json")
            .acceptHeader("application/json");


//    {
//        setUp(
//                Scenarios.defaultScenario.injectOpen(
//                        atOnceUsers(1),
//                        nothingFor(5),
//                        rampUsers(5).during(20),
//                        nothingFor(5),
//                        constantUsersPerSec(1).during(5)),
//                Scenarios.adminScenario.injectOpen(
//                        atOnceUsers(1),
//                        nothingFor(5),
//                        rampUsers(5).during(20),
//                        nothingFor(5),
//                        constantUsersPerSec(1).during(5)))
//                .protocols(HTTP_PROTOCOL);
//    }

//    {
//        setUp(scn.injectClosed(
//                rampConcurrentUsers(1).to(5).during(20),
//                constantConcurrentUsers(5).during(20)))
//                .protocols(httpProtocol);
//    }

//    {
//        setUp(scn.injectOpen(
//                constantUsersPerSec(2).during(180)))
//                .protocols(httpProtocol)
//                .throttle(
//                        reachRps(10).in(30),
//                        holdFor(60),
//                        jumpToRps(20),
//                        holdFor(60))
//                .maxDuration(180);
//    }

    {
        setUp(
            Scenarios.adminScenario.injectOpen(
                atOnceUsers(1)),
            Scenarios.defaultScenario.injectOpen(
                atOnceUsers(1)))
            .protocols(HTTP_PROTOCOL);
    }
}
