package ru.my.games.vertx;

import io.vertx.core.Vertx;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NotNullNullableValidation")
public final class BadVertxTest {

  private final Vertx vertx = Vertx.vertx();

  @Test
  public void badTest()  {
    vertx.createHttpServer()
      .requestHandler(req -> req.response().end("Ok"))
      .listen(16969, result -> {
        Assertions.assertTrue(result.succeeded());
      });
  }

  @Test
  public void badTest2() throws InterruptedException {
    vertx.createHttpServer()
      .requestHandler(req -> req.response().end("Ok"))
      .listen(16969, result -> {
        Assertions.fail();
      });
    Thread.sleep(5000);
  }
}
