package ru.my.games.example.eventbus;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import ru.vk.example.Party;

@SuppressWarnings("NotNullNullableValidation")
public final class PartyLauncher {
  public static void main(String[] args) {
    Vertx.clusteredVertx(
      new VertxOptions(),
      vertxResult -> {
        final var options = new DeploymentOptions().setWorker(true);
        vertxResult.result().deployVerticle(new Party(), options);
      }
    );
  }
}
