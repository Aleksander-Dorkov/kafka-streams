package com.example.kafkastreams.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumer {

    @KafkaListener(topics = KafkaTopics.GREETINGS_UPPER_CASE)
    public void listen(String key, String value) {
        System.out.println("@KafkaListener Received message - Key: " + key + ", Value: " + value);
    }

    @KafkaListener(topics = KafkaTopics.GENERAL_ORDERS)
    public void listen1(String key, String value) {
        System.out.println("@KafkaListener GENERAL_ORDERS Received message - Key: " + key + ", Value: " + value);
    }

    @KafkaListener(topics = KafkaTopics.RESTAURANT_ORDERS)
    public void listen2(String key, String value) {
        System.out.println("@KafkaListener RESTAURANT_ORDERS Received message - Key: " + key + ", Value: " + value);
    }
}
