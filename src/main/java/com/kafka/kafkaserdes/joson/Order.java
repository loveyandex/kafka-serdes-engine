package com.kafka.kafkaserdes.joson;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.kafka.common.errors.SerializationException;

import lombok.Getter;

@Getter
public class Order extends JSONSerdeAbs<Order> {

      public Order() {
      }

      public Long timestamp;

      public Order(Long timestamp, String symbol, String type, String side, String status, BigDecimal amount,
                  BigDecimal price) {
            this.timestamp = timestamp;
            this.symbol = symbol;
            this.type = type;
            this.side = side;
            this.status = status;
            this.amount = amount;
            this.price = price;
      }

      String symbol;
      String type;

      String side;
      String status;

      BigDecimal amount;
      BigDecimal price;

      public Order(String symbol, String type, String side, String status, BigDecimal amount, BigDecimal price) {
            this.symbol = symbol;
            this.type = type;
            this.side = side;
            this.status = status;
            this.amount = amount;
            this.price = price;
      }

      @SuppressWarnings("unchecked")
      @Override
      public Order deserialize(final String topic, final byte[] data) {
            if (data == null) {
                  return null;
            }

            try {
                  return (Order) JSONSerdeAbs.OBJECT_MAPPER.readValue(data, Order.class);
            } catch (final IOException e) {
                  throw new SerializationException(e);
            }
      }

}
