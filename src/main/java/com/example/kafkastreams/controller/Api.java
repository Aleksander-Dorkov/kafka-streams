package com.example.kafkastreams.controller;

import com.example.kafkastreams.kafka.KafkaProducer;
import com.example.kafkastreams.kafka.dto.Greeting;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
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
        this.kafkaProducer.publishGreetingOne("First" + counter1++);
    }

    @PostMapping("/greetings_two")
    public void publish2() {
        this.kafkaProducer.publishGreetingTwo("Second" + counter2++);
    }

    @PostMapping("/greetings_as_json")
    public void publish2(@RequestBody Greeting greeting) throws JsonProcessingException {
        this.kafkaProducer.publishGreetingOne(greeting);
    }

    @PostMapping("/order_as_json")
    public void publish3() {
        this.kafkaProducer.publishDummyOrders();
    }

    @PostMapping("/word_as_string_with_random_key/{word}")
    public void publish4(@PathVariable String word) {
        this.kafkaProducer.publishWord(word);
    }

    @PostMapping("/word_as_string/{key}/{value}")
    public void publish4(@PathVariable String key, @PathVariable String value) {
        this.kafkaProducer.publishWord(key, value);
    }

    @PostMapping("/aggregate_count_as_string/{key}/{value}")
    public void publish5(@PathVariable String key, @PathVariable String value) {
        this.kafkaProducer.publishAggregateCount(key, value);
    }

    @PostMapping("/aggregate_reduce_as_string/{key}/{value}")
    public void publish6(@PathVariable String key, @PathVariable String value) {
        this.kafkaProducer.publishAggregateReduce(key, value);
    }
}
