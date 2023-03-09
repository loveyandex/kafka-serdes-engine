package com.kafka.kafkaserdes;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.kafkaserdes.joson.KafkaPrdcrPageView;
import com.kafka.kafkaserdes.joson.PageView;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class Controller {

    private KafkaPrdcrPageView kppv;

    @PostMapping("/pv")
    public void addMessaged(@RequestBody PageView message) {
        kppv.sendMessage(message);
        System.out.println(message);

    }

}
