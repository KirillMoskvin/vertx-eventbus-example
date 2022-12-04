package ru.vk.example.shared.eventbus.stove;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

@SuppressWarnings("NotNullNullableValidation")
public final class SharedDataLauncher {

  public static void main(String[] args) {
    final var vertx = Vertx.vertx();
    final var options = new DeploymentOptions().setWorker(true);
    vertx.deployVerticle(new Auditor(), options);
    vertx.deployVerticle(new Stove(), options);
    vertx.deployVerticle(new Stove(), options);
  }
}