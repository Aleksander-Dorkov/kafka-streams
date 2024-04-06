package com.example.kafkastreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@EnableKafkaStreams
@SpringBootApplication
public class KafkaStreamsApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(KafkaStreamsApplication.class, args);
    }
}
