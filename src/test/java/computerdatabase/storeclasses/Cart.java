package computerdatabase.storeclasses;

import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public final class Cart {

    public static final ChainBuilder addToCart =
            exec(
                    Product.chooseProduct,
                    http("Add product to cart - #{name}")
                            .get("/cart/add/#{id}")
                            .check(regex("items in your cart").exists()))
                    .exec(session -> {
                        double itemPrice = session.getDouble("price");
                        double newTotal = session.getDouble("totalCart") + itemPrice;
                        return session.set("totalCart", newTotal);
                    });

    public static final ChainBuilder viewCart =
            doIf(session -> !session.getBoolean("userLogged"))
                    .then(exec(Login.login))
                    .exec(http("Open cart")
                            .get("/cart/view")
                            .check(css("#grandTotal").isEL("$#{totalCart}")));

    public static final ChainBuilder checkout = exec(
            http("Checkout")
                    .get("/cart/checkout")
                    .check(regex("<h3 class=\"display-3\">Checkout Confirmation</h3>").exists())
    );
}
