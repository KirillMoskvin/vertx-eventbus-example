package ru.vk.example.shared.eventbus.stove;

import io.vertx.core.AbstractVerticle;

import java.util.Comparator;
import java.util.Map;

import static java.lang.System.lineSeparator;

@SuppressWarnings({"MagicNumber", "UseOfSystemOutOrSystemErr", "NotNullNullableValidation"})
public final class Auditor extends AbstractVerticle {

  private static Comparator<Map.Entry<String, Long>> alphabetically =
    Comparator.<Map.Entry<String, Long>, Integer>comparing(e -> e.getKey().length())
      .thenComparing(Map.Entry::getKey);

  @Override
  public void start() {
    System.out.println("Start auditor");

    vertx.setPeriodic(5000, timer ->
      vertx.sharedData().<String, Long>getAsyncMap("cookies", map ->
        map.result().entries(cookies -> {
          if (cookies.result().isEmpty()) {
            System.out.println("Audition found no cookies :(");
            return;
          }
          final StringBuilder builder = new StringBuilder("=======Audition:=======").append(lineSeparator());
          cookies.result().entrySet().stream()
            .sorted(alphabetically)
            .forEach(entry -> {
                final var stove = entry.getKey();
                final var cookiesCount = entry.getValue();
                builder.append(stove).append(" did ").append(cookiesCount).append(" cookies (auditor)").append(lineSeparator());
              }
            );
          builder.append("=======================");
          System.out.println(builder);
        })
      )
    );
  }
}