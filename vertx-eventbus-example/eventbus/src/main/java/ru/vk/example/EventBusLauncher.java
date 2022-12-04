package ru.vk.example;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

@SuppressWarnings("NotNullNullableValidation")
public final class EventBusLauncher {

  public static void main(String[] args) {
    final var vertx = Vertx.vertx();

    final var factory = new Member.Factory();
    vertx.registerVerticleFactory(factory);
    final DeploymentOptions optionsMember = new DeploymentOptions().setWorker(true).setInstances(5);
    vertx.deployVerticle(
      factory.prefix() + ':' + Member.class.getName(),
      optionsMember,
      res -> System.out.println("Members deploy result: " + res.succeeded())
    );

    vertx.deployVerticle(new Party(), new DeploymentOptions().setWorker(true));
  }
}
