package com.kafka.kafkaserdes.runs;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Consumed;

import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.KafkaStreams;

import com.kafka.kafkaserdes.joson.Order;

public class OrderStreamProccessing {
      public static void main(final String[] args) throws Exception {
            Properties props = new Properties();
            props.put(StreamsConfig.APPLICATION_ID_CONFIG, "orderbook-app");
            props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
            props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
            props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Order.class);

            StreamsBuilder builder = new StreamsBuilder();

            KStream<String, Order> orderStream = builder.stream("btc_usdt-orderbooks");

            KStream<String, Order> orderStreamlimit = orderStream
                        .filter((key, value) -> value.getType().equals("limit"));

            orderStreamlimit.to("btc_usdt-limit-orderbooks",
                        Produced.with(Serdes.String(), new Order()));

            KafkaStreams streams = new KafkaStreams(builder.build(), props);
            streams.start();
      }

}
