package com.learning.vertx.vertx_stock_broker.broker.assets;

import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AssetsRestApi {

  public static void attach(final Router parent) {
    parent.get("/assets").handler(context -> {
      final var response = new JsonArray();
      response
        .add(new Asset("AAPL"))
        .add(new Asset("AMZN"))
        .add(new Asset("NFLX"))
        .add(new Asset("TSLA"));
      log.info("Path {} responds with {}", context.normalizedPath(), response.encode());
      context.response().end(response.toBuffer());
    });
  }
}
