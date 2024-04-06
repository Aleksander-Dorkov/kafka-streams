package com.example.kafkastreams.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumer {

    @KafkaListener(topics = KafkaTopics.GREETINGS_UPPER_CASE)
    public void listen(String key, String value) {
        System.out.println("@KafkaListener Received message - Key: " + key + ", Value: " + value);
    }
}
