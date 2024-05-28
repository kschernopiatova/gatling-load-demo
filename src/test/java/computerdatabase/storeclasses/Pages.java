package computerdatabase.storeclasses;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public final class Pages {

    public static final ChainBuilder homePage = exec(
            http("Load Home Page")
                    .get("/")
                    .check(status().is(200))
                    .check(regex("<title>Gatling Demo-Store</title>").exists())
                    .check(css("#_csrf", "content").saveAs("csrf")));

    public static final ChainBuilder aboutUsPage = exec(
            http("Load About Us Page")
                    .get("/about-us")
                    .check(regex("<h2>About Us</h2>").exists())
                    .check(status().is(200))
    );
}
