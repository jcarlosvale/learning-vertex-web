package com.learning.vertx.vertx_stock_broker.broker.quotes;

import com.learning.vertx.vertx_stock_broker.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class QuotesRestApi {

  public static void attach(Router parent) {
    parent.get("/quotes/:asset").handler(context -> {
      final String assetParam = context.pathParam("asset");
      log.info("Asset parameter: {}", assetParam);

      var quote = initRandomQuote(assetParam);

      final JsonObject response = quote.toJsonObject();
      log.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
    });
  }

  private static Quote initRandomQuote(String assetParam) {
    return Quote.builder()
      .asset(new Asset(assetParam))
      .volume(randomValue())
      .ask(randomValue())
      .bid(randomValue())
      .lastPrice(randomValue())
      .build();
  }

  private static BigDecimal randomValue() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(1, 100));
  }
}
