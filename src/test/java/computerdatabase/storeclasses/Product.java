package computerdatabase.storeclasses;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.jsonFile;
import static io.gatling.javaapi.http.HttpDsl.http;

public final class Product {

    private static final FeederBuilder<String> categoryFeeder =
            csv("data/categories.csv").random();

    private static final FeederBuilder<Object> productFeeder =
            jsonFile("data/productDetails.json").random();

    public static final ChainBuilder chooseCategory = feed(categoryFeeder).exec(
            http("Open Category - #{CategoryName}")
                    .get("/category/#{CategoryUrl}")
    );

    public static final ChainBuilder chooseProduct = feed(productFeeder).exec(
            http("Open Product Page - #{name}")
                    .get("/product/#{slug}")
                    .check(css("#ProductDescription p").isEL("#{description}"))
    );
}
