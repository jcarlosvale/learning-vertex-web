package com.learning.vertx.vertx_stock_broker.broker.quotes;

import com.learning.vertx.vertx_stock_broker.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Quote {

  Asset asset;
  BigDecimal bid;
  BigDecimal ask;
  BigDecimal lastPrice;
  BigDecimal volume;

  public JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }

}
