package computerdatabase.storeclasses;

import computerdatabase.storeclasses.Cart;
import computerdatabase.storeclasses.Pages;
import computerdatabase.storeclasses.Product;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.flushCookieJar;

public class Scenarios {

    private static final ChainBuilder initSession =
            exec(flushCookieJar())
                    .exec(session -> session.set("userLogged", false))
                    .exec(session -> session.set("totalCart", 0.0));

    private static final ChainBuilder completePurchase =
            exec(
                    initSession,
                    Pages.homePage,
                    pause(2),
                    Pages.aboutUsPage,
                    pause(2),
                    Product.chooseCategory,
                    pause(2),
                    Cart.addToCart,
                    pause(2),
                    Cart.viewCart,
                    pause(2),
                    Cart.checkout
            );

    private static final ChainBuilder exploreProducts =
            exec(
                    initSession,
                    Pages.homePage,
                    pause(2),
                    Product.chooseCategory,
                    pause(2),
                    Product.chooseProduct,
                    pause(2),
                    Product.chooseCategory,
                    pause(2),
                    Product.chooseCategory,
                    pause(2),
                    Product.chooseProduct
            );

    private static final ChainBuilder viewCart =
            exec(initSession)
                    .exec(Pages.homePage)
                    .pause(2)
                    .exec(Product.chooseCategory)
                    .pause(2)
                    .exec(Cart.addToCart)
                    .pause(2)
                    .exec(Product.chooseCategory)
                    .pause(2)
                    .exec(Cart.addToCart)
                    .pause(2)
                    .exec(Cart.viewCart);

    public static final ScenarioBuilder defaultScenario = scenario("Default store scenario")
            .during(Duration.ofSeconds(60))
            .on(randomSwitch()
                .on(
                        new Choice.WithWeight(75.0, exec(completePurchase)),
                        new Choice.WithWeight(15.0, exec(viewCart)),
                        new Choice.WithWeight(10.0, exec(exploreProducts))));

}
