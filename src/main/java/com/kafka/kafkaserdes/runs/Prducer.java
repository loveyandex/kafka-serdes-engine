package com.kafka.kafkaserdes.runs;

import com.kafka.kafkaserdes.joson.*;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import java.math.BigDecimal;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;

public class Prducer {

      public static void main(String[] args) throws InterruptedException {
            Properties props = new Properties();
            props.put(ProducerConfig.CLIENT_ID_CONFIG, " SystemConfig.producerApplicationID");
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                        "localhost:9091,localhost:9092,localhost:9093,localhost:9094,localhost:9095");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        "org.apache.kafka.common.serialization.StringSerializer");
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Order.class);

            KafkaProducer<String, Order> producer = new KafkaProducer<String, Order>(props);

            while (true) {
                  BigDecimal bd1 = new BigDecimal(Math.random());
                  BigDecimal bd2 = new BigDecimal(Math.random()*10000+9999);

                  Order x = new Order(System.currentTimeMillis(),"BTC_USDT", "limit", "buy", "open", bd1,bd2);
                  ProducerRecord<String, Order> record = new ProducerRecord<String, Order>(
                              "btc_usdt-orderbooks", "btc_usdt", x);

                  try {
                        RecordMetadata recordMetadata = producer.send(record).get();
                        System.out.println(record);
                  } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                  } catch (ExecutionException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                  }

                  Thread.sleep(10);

            }

      }
}
