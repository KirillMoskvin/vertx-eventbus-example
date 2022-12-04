package ru.my.games.example.shared;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import ru.vk.example.shared.eventbus.stove.Stove;

@SuppressWarnings("NotNullNullableValidation")
public final class StoveLauncher {

  public static void main(String[] args) {
    Vertx.clusteredVertx(
      new VertxOptions(),
      vertxResult -> {
        final var vertx = vertxResult.result();
        final var options = new DeploymentOptions().setWorker(true);
        vertx.deployVerticle(new Stove(), options);
        vertx.deployVerticle(new Stove(), options);
        vertx.deployVerticle(new Stove(), options);
      }
    );
  }
}
