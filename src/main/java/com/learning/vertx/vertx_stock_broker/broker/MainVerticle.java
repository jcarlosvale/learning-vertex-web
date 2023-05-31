package com.learning.vertx.vertx_stock_broker.broker;

import com.learning.vertx.vertx_stock_broker.broker.config.ConfigLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  public static final int PORT = 8888;

  public static void main(String[] args) {
    System.setProperty(ConfigLoader.SERVER_PORT, "9000");
    var vertx = Vertx.vertx();
    vertx.exceptionHandler(error -> log.error("Unhandled error: {0}", error));
    vertx.deployVerticle(new MainVerticle())
      .onFailure(err -> log.error("Failed to deploy: " + err))
      .onSuccess(id -> log.info("Deployed {} with id {}", MainVerticle.class.getSimpleName(), id));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(RestAPIVerticle.class.getName(),
        new DeploymentOptions()
          .setInstances(Runtime.getRuntime().availableProcessors()))
      .onFailure(startPromise::fail)
      .onSuccess(id -> {
        log.info("Deployed {} with id {}", RestAPIVerticle.class.getSimpleName(), id);
        startPromise.complete();
      });
  }
}
