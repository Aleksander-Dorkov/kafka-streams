package com.example.kafkastreams.controller;

import com.example.kafkastreams.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Api {

    private int counter1 = 0;
    private int counter2 = 0;
    private final KafkaProducer kafkaProducer;

    @PostMapping("/greetings_one")
    public void publish() {
        this.kafkaProducer.sendMessageOne("First" + counter1++);
    }

    @PostMapping("/greetings_two")
    public void publish2() {
        this.kafkaProducer.sendMessageTwo("Second" + counter2++);
    }
}
