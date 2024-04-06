package com.example.kafkastreams.controller;

import com.example.kafkastreams.kafka.KafkaProducer;
import com.example.kafkastreams.kafka.dto.Greeting;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/greetings_as_json")
    public void publish2(@RequestBody Greeting greeting) throws JsonProcessingException {
        this.kafkaProducer.sendMessageOne(greeting);
    }
}
