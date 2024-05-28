package computerdatabase.apistoreclasses;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public final class Authentication {

    public static final ChainBuilder authenticate =
            doIf(session -> !session.getBoolean("Authenticated")).then(
                    exec(http("Authenticate")
                            .post("/api/authenticate")
                            .body(RawFileBody("api_requests/authentication.json"))
                            .check(status().is(200))
                            .check(jmesPath("token").saveAs("token")))
                            .exec(session -> session.set("Authenticated", true)));
}
