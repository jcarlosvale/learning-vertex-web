package com.learning.vertx.vertx_stock_broker.broker.watchlist;

import com.learning.vertx.vertx_stock_broker.broker.MainVerticle;
import com.learning.vertx.vertx_stock_broker.broker.assets.Asset;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ExtendWith(VertxExtension.class)
class TestWatchListRestApi {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void adds_and_returns_watchList_for_account(Vertx vertx, VertxTestContext testContext) {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    var accountId = UUID.randomUUID();
    client.put("/account/watchlist/" + accountId)
      .sendJsonObject(body())
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        log.info("Response PUT: {}", json);
        assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode());
        assertEquals(200, response.statusCode());
      })).compose(next -> {
        client.get("/account/watchlist/" + accountId)
          .send()
          .onComplete(testContext.succeeding(response -> {
            var json = response.bodyAsJsonObject();
            log.info("Response GET: {}", json);
            assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode());
            assertEquals(200, response.statusCode());
            testContext.completeNow();
          }));
        return Future.succeededFuture();
      });
  }

  @Test
  void adds_and_deletes_watchList_for_account(Vertx vertx, VertxTestContext testContext) {
    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    var accountId = UUID.randomUUID();
    client.put("/account/watchlist/" + accountId)
      .sendJsonObject(body())
      .onComplete(testContext.succeeding(response -> {
        var json = response.bodyAsJsonObject();
        log.info("Response PUT: {}", json);
        assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode());
        assertEquals(200, response.statusCode());
      })).compose(next -> {
        client.delete("/account/watchlist/" + accountId)
          .send()
          .onComplete(testContext.succeeding(response -> {
            var json = response.bodyAsJsonObject();
            log.info("Response DELETE: {}", json);
            assertEquals("{\"assets\":[{\"name\":\"AMZN\"},{\"name\":\"TSLA\"}]}", json.encode());
            assertEquals(200, response.statusCode());
            testContext.completeNow();
          }));
        return Future.succeededFuture();
      });
  }

  private static JsonObject body() {
    return new WatchList(Arrays.asList(
      new Asset("AMZN"),
      new Asset("TSLA"))
    ).toJsonObject();
  }
}
