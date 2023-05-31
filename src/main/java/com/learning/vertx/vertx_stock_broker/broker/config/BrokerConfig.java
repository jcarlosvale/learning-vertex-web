package com.learning.vertx.vertx_stock_broker.broker.config;

import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import static com.learning.vertx.vertx_stock_broker.broker.config.ConfigLoader.SERVER_PORT;

@Builder
@Value
@ToString
public class BrokerConfig {

  int serverPort;

  public static BrokerConfig from(final JsonObject config) {

    final var serverPort = config.getInteger(SERVER_PORT);
    if (serverPort == null) {
      throw new RuntimeException(SERVER_PORT + " not configured!!");
    }

    return BrokerConfig.builder()
      .serverPort(serverPort)
      .build();
  }
}
