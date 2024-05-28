package computerdatabase.apistoreclasses;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import java.util.List;
import java.util.Random;

import static computerdatabase.apistoreclasses.Headers.authorizationHeader;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public final class Category {

    private static final FeederBuilder<String> categoryFeeder =
            csv("api_data/categories.csv").random();

    public static final ChainBuilder list =
            exec(http("List categories")
                    .get("/api/category")
                    .check(jsonPath("$[?(@.id == 7)].name").is("Unisex"))
                    .check(jmesPath("[].id").ofList().saveAs("categoryIds")));

    public static final ChainBuilder update = feed(categoryFeeder)
            .exec(session -> {
                List<Integer> categoryIds = session.getList("categoryIds");
                Integer categoryId = categoryIds.get(new Random().nextInt(categoryIds.size()));
                return session.set("categoryId", categoryId);
            })
            .exec(Authentication.authenticate)
            .exec(http("Change category by id - #{categoryId}")
                    .put("/api/category/#{categoryId}")
                    .headers(authorizationHeader)
                    .body(ElFileBody("api_requests/update-category.json"))
                    .check(status().is(200))
                    .check(jmesPath("name").isEL("#{categoryName}")))
            .exec(session -> session.set("Authenticated", true));
}
