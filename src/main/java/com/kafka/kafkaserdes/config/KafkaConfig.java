package com.kafka.kafkaserdes.config;


import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.STATE_DIR_CONFIG;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.TopicBuilder;

import com.kafka.kafkaserdes.joson.JSONSerde; 

@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaConfig {

      @Value(value = "${spring.kafka.bootstrap-servers}")
      private String bootstrapAddress;

      @Value(value = "${server.port}")
      private String serverPort;

      @Value(value = "${spring.application.name}")
      private String springApplicationName;

      // @Value(value = "${spring.kafka.streams.state.dir}")
      // private String stateStoreLocation;

      @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
      KafkaStreamsConfiguration kStreamsConfig() {
            Map<String, Object> props = new HashMap<>();
            props.put(APPLICATION_ID_CONFIG, "streams-app");
            props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
            // props.put("key.serializer", Serdes.String().getClass().getName());
            // props.put("value.serializer", JSONSerde.class.getName());
            // configure the state location to allow tests to use clean state for every run
            // props.put(STATE_DIR_CONFIG, stateStoreLocation);
            props.put(STATE_DIR_CONFIG, String.format("%s%s", springApplicationName, serverPort));

            return new KafkaStreamsConfiguration(props);
      }

      @Bean
      NewTopic inputTopic() {
            return TopicBuilder.name("input-topic")
                        .partitions(1)
                        .replicas(1)
                        .build();
      }

}
