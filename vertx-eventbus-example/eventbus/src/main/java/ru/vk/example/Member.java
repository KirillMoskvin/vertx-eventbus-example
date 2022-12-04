package ru.vk.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.impl.JavaVerticleFactory;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings({"UseOfSystemOutOrSystemErr", "NotNullNullableValidation"})
public final class Member extends AbstractVerticle {
  private final String name;

  private Member(long number) {
    this.name = "member#" + number;
  }

  @Override
  public void start() {
    subscribe();
  }

  private void subscribe() {
    final JsonObject message = new JsonObject().put("name", name);
    System.out.println(name + " wants to join the party");

    vertx.eventBus().consumer("party.started", event -> vertx.eventBus().send("party.join", message));

    vertx.eventBus().<String>consumer("host-congratulations", event -> {
      final String to = event.body();
      if (to.equals(name)) {
        System.out.println('[' + name + "]: Yippee!");
      } else {
        sendCongratulations(to);
      }
    });

    vertx.eventBus().consumer("member-congratulations" + name, event -> {
        if (ThreadLocalRandom.current().nextBoolean()) {
          event.reply("Thanks!");
        }
      }
    );
  }

  private void sendCongratulations(String to) {
    final DeliveryOptions options = new DeliveryOptions().setSendTimeout(1000);
    vertx.eventBus().request("member-congratulations" + to, "Let's groove!", options, reply -> {
      if (reply.succeeded()) {
        System.out.println("Hangs out with " + to);
      } else {
        System.out.println("Bored of " + to + ", cause: " + reply.cause());
      }
    });
  }

  public static final class Factory extends JavaVerticleFactory {
    private long number;

    public Factory() {
      this(0);
    }

    public Factory(int startNumber) {
      number = startNumber;
    }

    @Override
    public String prefix() {
      return "sphere";
    }


    @Override
    public void createVerticle(String verticleName,
                               ClassLoader classLoader,
                               Promise<Callable<Verticle>> promise) {
      promise.complete(() -> new Member(number++));
    }
  }
}
