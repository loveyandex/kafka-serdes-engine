package com.kafka.kafkaserdes.joson;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.TimeWindows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

 
/**
 * A serde for any class that implements {@link JSONSerdeCompatible}. Note that
 * the classes also need to
 * be registered in the {@code @JsonSubTypes} annotation on
 * {@link JSONSerdeCompatible}.
 *
 * @param <T> The concrete type of the class that gets de/serialized
 */
public class JSONSerde<T> implements Serializer<T>, Deserializer<T>, Serde<T> {
      private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

      @Override
      public void configure(final Map<String, ?> configs, final boolean isKey) {
      }

      @SuppressWarnings("unchecked")
      @Override
      public T deserialize(final String topic, final byte[] data) {
            if (data == null) {
                  return null;
            }

            try {
                  return (T) OBJECT_MAPPER.readValue(data, JSONSerdeCompatible.class);
            } catch (final IOException e) {
                  throw new SerializationException(e);
            }
      }

      @Override
      public byte[] serialize(final String topic, final T data) {
            if (data == null) {
                  return null;
            }

            try {
                  return OBJECT_MAPPER.writeValueAsBytes(data);
            } catch (final Exception e) {
                  throw new SerializationException("Error serializing JSON message", e);
            }
      }

      @Override
      public void close() {
      }

      @Override
      public Serializer<T> serializer() {
            return this;
      }

      @Override
      public Deserializer<T> deserializer() {
            return this;
      }
}
