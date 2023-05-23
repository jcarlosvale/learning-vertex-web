package com.learning.vertx.vertx_stock_broker.broker;

import com.learning.vertx.vertx_stock_broker.broker.assets.AssetsRestApi;
import com.learning.vertx.vertx_stock_broker.broker.config.BrokerConfig;
import com.learning.vertx.vertx_stock_broker.broker.config.ConfigLoader;
import com.learning.vertx.vertx_stock_broker.broker.quotes.QuotesRestApi;
import com.learning.vertx.vertx_stock_broker.broker.watchlist.WatchListRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestAPIVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(configuration -> {
        log.info("Retrieved Configuration: {}", configuration);
        startHttpServerAndAttachRoutes(startPromise, configuration);
      });
  }

  private void startHttpServerAndAttachRoutes(Promise<Void> startPromise, BrokerConfig configuration) {
    final Router restApi = Router.router(vertx);

    restApi.route()
      .handler(BodyHandler.create())
      .failureHandler(handleFailure());

    AssetsRestApi.attach(restApi);
    QuotesRestApi.attach(restApi);
    WatchListRestApi.attach(restApi);

    vertx.createHttpServer()
      .requestHandler(restApi)
      .exceptionHandler(error -> log.error("HTTP Server error: ", error))
      .listen(configuration.getServerPort(), http -> {
        if (http.succeeded()) {
          startPromise.complete();
          log.info("HTTP server started on port {}", configuration.getServerPort());
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private static Handler<RoutingContext> handleFailure() {
    return errorContext -> {
      if (errorContext.response().ended()) {
        //ignore completed response
        return;
      }
      log.error(" Route error: ", errorContext.failure());
      errorContext.response()
        .setStatusCode(500)
        .end(new JsonObject().put("message", "Something went wrong").toBuffer());
    };
  }

}
