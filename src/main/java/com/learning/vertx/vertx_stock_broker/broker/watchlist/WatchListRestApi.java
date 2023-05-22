package com.learning.vertx.vertx_stock_broker.broker.watchlist;


import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.UUID;

@Slf4j
public class WatchListRestApi {

  public static void attach(Router parent) {
    final HashMap<UUID, WatchList> watchListPerAccount = new HashMap<UUID, WatchList>();

    String path = "/account/watchlist/:accountId";
    parent.get(path).handler(new GetWatchListHandler(watchListPerAccount));
    parent.put(path).handler(new PutWatchListHandler(watchListPerAccount));
    parent.delete(path).handler(new DeleteWatchListHandler(watchListPerAccount));
  }

  public static String getAccountId(RoutingContext context) {
    var accountId = context.pathParam("accountId");
    log.debug("{} for account {}", context.normalizedPath(), accountId);
    return accountId;
  }
}
