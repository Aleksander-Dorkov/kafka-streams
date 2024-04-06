package com.example.kafkastreams.kafka;

import com.example.kafkastreams.kafka.dto.Greeting;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.example.kafkastreams.kafka.KafkaTopics.GREETINGS_ONE;
import static com.example.kafkastreams.kafka.KafkaTopics.GREETINGS_TWO;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final ObjectMapper objectMapper;
    ;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Random random = new Random();


    public void sendMessageOne(String message) {
        String key = String.valueOf(random.nextInt(Integer.MAX_VALUE));
        kafkaTemplate.send(GREETINGS_ONE, key, message);
    }

    public void sendMessageTwo(String message) {
        String key = String.valueOf(random.nextInt(Integer.MAX_VALUE));
        kafkaTemplate.send(GREETINGS_TWO, key, message);
    }

    public void sendMessageOne(Greeting message) throws JsonProcessingException {
        String key = String.valueOf(random.nextInt(Integer.MAX_VALUE));
        kafkaTemplate.send(GREETINGS_ONE, key, objectMapper.writeValueAsString(message));
    }
}
