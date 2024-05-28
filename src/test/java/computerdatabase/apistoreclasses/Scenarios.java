package computerdatabase.apistoreclasses;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.exec;

public class Scenarios {

    private static final Duration MIN_PAUSE = Duration.ofMillis(500);
    private static final Duration MAX_PAUSE = Duration.ofSeconds(4);

    private static final ChainBuilder initSession =
            exec(session -> session.set("Authenticated", false));

    private static class UserJourneys {
        private static final ChainBuilder admin =
                exec(initSession)
                        .exec(Category.list)
                        .pause(MIN_PAUSE, MAX_PAUSE)
                        .exec(Product.listByCategory)
                        .pause(MIN_PAUSE, MAX_PAUSE)
                        .exec(Product.get)
                        .pause(MIN_PAUSE, MAX_PAUSE)
                        .exec(Product.update)
                        .pause(MIN_PAUSE, MAX_PAUSE)
                        .repeat(3).on(exec(Product.create))
                        .pause(MIN_PAUSE, MAX_PAUSE)
                        .exec(Category.update);

        private static final ChainBuilder user =
                exec(initSession)
                        .exec(Category.list)
                        .pause(MIN_PAUSE, MAX_PAUSE)
                        .exec(Product.listByCategory)
                        .pause(MIN_PAUSE, MAX_PAUSE)
                        .repeat(3).on(exec(Product.get));

        private static final ChainBuilder priceChanger =
                exec(initSession)
                        .exec(Product.listAll)
                        .repeat("#{productIds.size()}", "index").on(
                                exec(session -> {
                                    int index = session.getInt("index");
                                    List<Object> allProducts = session.getList("productIds");
                                    return session.set("product", allProducts.get(index));
                                })
                                        .exec(Product.update));
    }
        public static ScenarioBuilder defaultScenario = scenario("Default store api scenario")
                .during(Duration.ofSeconds(60))
                .on(randomSwitch()
                        .on(
                                new Choice.WithWeight(65.0, exec(UserJourneys.user)),
                                new Choice.WithWeight(20.0, exec(UserJourneys.admin)),
                                new Choice.WithWeight(15.0, exec(UserJourneys.priceChanger))));

        public static ScenarioBuilder adminScenario = scenario("Admin usage store api scenario")
                .during(Duration.ofSeconds(60))
                .on(randomSwitch()
                        .on(
                                new Choice.WithWeight(70.0, exec(UserJourneys.admin)),
                                new Choice.WithWeight(30.0, exec(UserJourneys.priceChanger))));
}
