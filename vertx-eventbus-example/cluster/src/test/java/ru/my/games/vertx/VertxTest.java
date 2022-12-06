package ru.my.games.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.vk.example.Party;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("NotNullNullableValidation")
@ExtendWith(VertxExtension.class)
public final class VertxTest {

  @Test
  void testWithContext(Vertx vertx, VertxTestContext testContext) throws InterruptedException {
    vertx.createHttpServer()
      .requestHandler(req -> req.response().end("Ok"))
      .listen(16969)
      .onComplete(testContext.succeedingThenComplete());
    testContext.awaitCompletion(1, TimeUnit.SECONDS);
    if (testContext.failed()) {
      Assertions.fail(testContext.causeOfFailure());
    }
  }

  @Test
  @Timeout(value = 1, timeUnit = TimeUnit.SECONDS)
  void testWithCheckpoints(Vertx vertx, VertxTestContext testContext) {
    final var deployed = testContext.checkpoint();
    final var msgGet = testContext.checkpoint();

    vertx.eventBus().consumer("party.started", __ -> msgGet.flag());
    vertx.deployVerticle(new Party())
        .onComplete(__ -> deployed.flag());
  }

  @Test
  void countdownLatch(Vertx vertx) throws InterruptedException {
    final var countDown = new CountDownLatch(10);
    vertx.eventBus().consumer("party.started", __ -> countDown.countDown())
      .completionHandler(__ -> vertx.deployVerticle(new Party(), new DeploymentOptions().setInstances(10)));
    Assertions.assertTrue(countDown.await(1, TimeUnit.SECONDS));
  }
}
