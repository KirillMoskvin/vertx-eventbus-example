package ru.vk.example.shared.eventbus.stove;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.LockSupport;

import static java.util.Objects.requireNonNullElse;

@SuppressWarnings({"MagicNumber", "UseOfSystemOutOrSystemErr", "NotNullNullableValidation"})
public final class Stove extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.sharedData().getCounter("stoveNumber", counter -> {
      if (counter.succeeded()) {
        counter.result().incrementAndGet(number -> turnOn("Stove#" + number.result()));
        startPromise.complete();
      }
    });
  }

  private static long bakeCookies() {
    final ThreadLocalRandom random = ThreadLocalRandom.current();
    final long secondsToBake = random.nextInt(5);
    LockSupport.parkNanos(secondsToBake * 1_000_000_000L);
    final long cookies = random.nextLong(50) + 1;
    System.out.println(cookies + " cookies are ready after " + secondsToBake + " seconds");
    return cookies;
  }

  private void turnOn(String stove) {
    System.out.println("Turn on stove " + stove);
    vertx.setPeriodic(5000, timer ->
      vertx.<Long>executeBlocking(
        promise -> promise.complete(bakeCookies()),
        cookies -> report(stove, cookies.result())
      )
    );
  }

  private void report(String stove, Long cookiesCount) {
    vertx.sharedData().<String, Long>getAsyncMap("cookies", map ->
      map.result().get(stove, getResult ->
        map.result().put(stove, requireNonNullElse(getResult.result(), 0L) + cookiesCount,
          completion -> System.out.println("Reported " + cookiesCount + " cookies from " + stove)
        )
      )
    );
  }
}
