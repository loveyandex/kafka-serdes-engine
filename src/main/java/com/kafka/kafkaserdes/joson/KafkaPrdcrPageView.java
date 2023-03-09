package com.kafka.kafkaserdes.joson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class KafkaPrdcrPageView {

      @Autowired
      private final KafkaTemplate<String, PageView> pafeviewProducer;

      public void sendMessage(PageView message) {

            // logger.info(String.format("input-topic Message sent -> %s", message));
            pafeviewProducer.send("streams-pageview-input", "key1", message);
      }
}