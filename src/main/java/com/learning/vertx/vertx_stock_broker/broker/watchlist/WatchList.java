package com.learning.vertx.vertx_stock_broker.broker.watchlist;

import com.learning.vertx.vertx_stock_broker.broker.assets.Asset;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {

  List<Asset> assets;

  JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }

}
