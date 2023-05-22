package com.learning.vertx.vertx_stock_broker.broker.quotes;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class GetQuoteHandler implements Handler<RoutingContext> {

  private final Map<String, Quote> cachedQuotes;

  public GetQuoteHandler(Map<String, Quote> cachedQuotes) {
    this.cachedQuotes = cachedQuotes;
  }

  @Override
  public void handle(RoutingContext context) {
    final String assetParam = context.pathParam("asset");
    log.info("Asset parameter: {}", assetParam);

    var optionalQuote = Optional.ofNullable(cachedQuotes.get(assetParam));
    if (optionalQuote.isEmpty()) {
      context
        .response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(
          new JsonObject()
            .put("message", "quote for asset " + assetParam + " not available")
            .put("path", context.normalizedPath())
            .toBuffer()
        );
      return;
    }

    final JsonObject response = optionalQuote.get().toJsonObject();
    log.info("Path {} responds with {}", context.normalizedPath(), response.encode());
    context.response().end(response.toBuffer());
  }
}
