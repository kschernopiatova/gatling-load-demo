package computerdatabase.storeclasses;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public final class Login {

    private static final FeederBuilder<String> userFeeder =
            csv("data/loginDetails.csv").random();

    public static final ChainBuilder login =
            feed(userFeeder)
                    .exec(
                            http("Open Login page")
                                    .get("/login")
                    )
                    .exec(
                            http("Login as user #{username}")
                                    .post("/login")
                                    .formParam("_csrf", "#{csrf}")
                                    .formParam("username", "#{username}")
                                    .formParam("password", "#{password}")
                                    .check(status().in(200, 302)))
                    .exec(session -> session.set("userLogged", true));
}
