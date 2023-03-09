package com.kafka.kafkaserdes.runs;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kafka.kafkaserdes.joson.JSONSerde;
import com.kafka.kafkaserdes.joson.JSONSerdePV;
import com.kafka.kafkaserdes.joson.PageView;
import com.kafka.kafkaserdes.joson.PageViewSerde;

public class Cnsumr {

      public static void main(String[] args) {

            Logger logger = LoggerFactory.getLogger(Cnsumr.class.getName());

            String grp_id = "third_app";
            Properties properties = new Properties();

            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                        "localhost:9091,localhost:9092,localhost:9093,localhost:9094,localhost:9095");
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JSONSerdePV.class);
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, grp_id);
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

            KafkaConsumer<String, PageView> consumer = new KafkaConsumer<String, PageView>(properties);
            // Subscribing
            consumer.subscribe(Arrays.asList("SystemConfig.topicName"));

            // polling
            while (true) {
                  ConsumerRecords<String, PageView> records = consumer.poll(Duration.ofMillis(100));
                  for (ConsumerRecord<String, PageView> record : records) {
                        logger.info("Key: " + record.key() + ", Value:" + record.value());

                        PageView value = record.value();
                        Timestamp ts = new Timestamp(System.currentTimeMillis());

                        Instant instant = ts.toInstant();

                        logger.info("Partition:" + record.partition() + ",Offset:" + record.offset());
                  }

            }

      }
}
