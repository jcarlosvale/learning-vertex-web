package com.learning.vertx.vertx_stock_broker.broker.watchlist;


import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class WatchListRestApi {

  public static void attach(Router parent) {
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();

    String path = "/account/watchlist/:accountId";
    parent.get(path).handler(context -> {
      String accountId = getAccountId(context);
      var watchList = Optional.ofNullable(watchListPerAccount.get(UUID.fromString(accountId)));

      if (watchList.isEmpty()) {
        context
          .response()
          .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(
            new JsonObject()
              .put("message", "watchlist for account " + accountId + " not available")
              .put("path", context.normalizedPath())
              .toBuffer()
          );
        return;
      }
      context.response().end(watchList.get().toJsonObject().toBuffer());
    });
    parent.put(path).handler(context -> {
      String accountId = getAccountId(context);

      var json = context.body().asJsonObject();
      var watchList = json.mapTo(WatchList.class);
      watchListPerAccount.put(UUID.fromString(accountId), watchList);
      context.response().end(json.toBuffer());
    });
    parent.delete(path).handler(context -> {
      String accountId = getAccountId(context);
      WatchList deleted = watchListPerAccount.remove(UUID.fromString(accountId));
      log.info("Deleted: {}, Remaining: {}", deleted, watchListPerAccount.values());
      context.response()
        .end(deleted.toJsonObject().toBuffer());
    });
  }

  private static String getAccountId(RoutingContext context) {
    var accountId = context.pathParam("accountId");
    log.debug("{} for account {}", context.normalizedPath(), accountId);
    return accountId;
  }
}
