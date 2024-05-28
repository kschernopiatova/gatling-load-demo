package computerdatabase.apistoreclasses;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static computerdatabase.apistoreclasses.Headers.authorizationHeader;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.jmesPath;
import static io.gatling.javaapi.http.HttpDsl.http;

public final class Product {

    private static final FeederBuilder<String> productFeeder =
            csv("api_data/products.csv").circular();

    public static final ChainBuilder listByCategory =
            exec(session -> {
                List<Integer> categoryIds = session.getList("categoryIds");
                Integer categoryId = categoryIds.get(new Random().nextInt(categoryIds.size()));
                return session.set("categoryId", categoryId);
            })
                    .exec(http("List products by category")
                            .get("/api/product?category=#{categoryId}")
                            .check(jmesPath("[? categoryId != '#{categoryId}']").ofList().is(Collections.emptyList()))
                            .check(jmesPath("[].id").ofList().saveAs("productIds")));

    public static final ChainBuilder listAll =
            exec(http("List all products")
                    .get("/api/product")
                    .check(jmesPath("[].id").ofList().saveAs("productIds")));

    public static final ChainBuilder get =
            exec(session -> {
                List<Integer> productIds = session.getList("productIds");
                Integer productId = productIds.get(new Random().nextInt(productIds.size()));
                return session.set("productId", productId);
            })
                    .exec(http("Get product by id - #{productId}")
                            .get("/api/product/#{productId}")
                            .check(jmesPath("id").isEL("#{productId}")));

    public static final ChainBuilder update = feed(productFeeder)
            .exec(session -> {
                List<Integer> productIds = session.getList("productIds");
                Integer productId = productIds.get(new Random().nextInt(productIds.size()));
                return session.set("productId", productId);
            })
            .exec(Authentication.authenticate)
            .exec(http("Change product by id - #{productId}")
                    .put("/api/product/#{productId}")
                    .headers(authorizationHeader)
                    .body(ElFileBody("api_requests/update-product.json"))
                    .check(jmesPath("name").isEL("#{productName}")));

    public static final ChainBuilder create =
            feed(productFeeder)
                    .exec(Authentication.authenticate)
                    .exec(http("Create new product - #{productName}")
                            .post("/api/product")
                            .headers(authorizationHeader)
                            .body(ElFileBody("api_requests/create-product-0.json"))
                            .check(jmesPath("price").isEL("#{productPrice}")));
}
