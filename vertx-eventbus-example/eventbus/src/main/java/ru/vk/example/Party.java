package ru.vk.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

@SuppressWarnings({"UseOfSystemOutOrSystemErr", "NotNullNullableValidation"})
public final class Party extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) {
    vertx.eventBus().<JsonObject>consumer(
      "party.join",
      event -> {
        final JsonObject member = event.body();
        final String name = member.getString("name");
        System.out.println(name + " joins the party");
        vertx.eventBus().send("host-congratulations", name);
      }
    ).completionHandler(result -> {
      System.out.println("Party started!");
      vertx.eventBus().publish("party.started", null);

      if (result.succeeded()) {
        startPromise.complete();
        return;
      }
      startPromise.fail(result.cause());
    });
  }
}
