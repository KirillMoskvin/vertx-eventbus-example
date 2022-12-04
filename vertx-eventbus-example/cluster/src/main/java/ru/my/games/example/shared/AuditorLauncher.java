package ru.my.games.example.shared;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import ru.vk.example.shared.eventbus.stove.Auditor;

@SuppressWarnings("NotNullNullableValidation")
public final class AuditorLauncher {

  public static void main(String[] args) {
    Vertx.clusteredVertx(
      new VertxOptions(),
      vertxResult -> {
        final var options = new DeploymentOptions().setWorker(true);
        vertxResult.result().deployVerticle(new Auditor(), options);
      }
    );
  }
}
