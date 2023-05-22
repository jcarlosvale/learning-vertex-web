package com.learning.vertx.vertx_stock_broker.broker.assets;

import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class AssetsRestApi {

  public static final List<String> ASSETS = Arrays.asList("AAPL", "AMZN", "FB", "GOOG", "MSFT", "NFLX", "TSLA");

  public static void attach(final Router parent) {
    parent.get("/assets").handler(new GetAssetsHandler());
  }
}
