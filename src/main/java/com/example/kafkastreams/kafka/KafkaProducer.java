package com.example.kafkastreams.kafka;

import com.example.kafkastreams.kafka.dto.Greeting;
import com.example.kafkastreams.kafka.dto.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static com.example.kafkastreams.kafka.KafkaTopics.AGGREGATE_COUNT;
import static com.example.kafkastreams.kafka.KafkaTopics.AGGREGATE_REDUCE;
import static com.example.kafkastreams.kafka.KafkaTopics.GREETINGS_ONE;
import static com.example.kafkastreams.kafka.KafkaTopics.GREETINGS_TWO;
import static com.example.kafkastreams.kafka.KafkaTopics.ORDERS;
import static com.example.kafkastreams.kafka.KafkaTopics.WORDS;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public void publishGreetingOne(String message) {
        kafkaTemplate.send(GREETINGS_ONE, key(), message);
    }

    public void publishGreetingTwo(String message) {
        kafkaTemplate.send(GREETINGS_TWO, key(), message);
    }

    public void publishGreetingOne(Greeting message) throws JsonProcessingException {
        kafkaTemplate.send(GREETINGS_ONE, key(), mapper.writeValueAsString(message));
    }

    public void publishDummyOrders() {
        getDummyOrders().forEach(order -> kafkaTemplate.send(ORDERS, key(), order));
    }

    public void publishWord(String word) {
        kafkaTemplate.send(WORDS, key(), word);
    }

    public void publishWord(String key, String value) {
        kafkaTemplate.send(WORDS, key, value);
    }

    public void publishAggregateCount(String key, String value) {
        kafkaTemplate.send(AGGREGATE_COUNT, key, value);
    }

    public void publishAggregateReduce(String key, String value) {
        kafkaTemplate.send(AGGREGATE_REDUCE, key, value);
    }

    private List<String> getDummyOrders() {
        var o1 = Order.builder()
                .orderId(1)
                .finalAmount(new BigDecimal("100.00"))
                .orderType(Order.OrderType.GENERAL)
                .orderLineItems(List.of("Item 1", "Item 2"))
                .orderDteTime(LocalDateTime.now())
                .build();

        var o2 = Order.builder()
                .orderId(2)
                .finalAmount(new BigDecimal("50.00"))
                .orderType(Order.OrderType.RESTAURANT)
                .orderLineItems(List.of("Dish 1", "Dish 2", "Dish 3"))
                .orderDteTime(LocalDateTime.now())
                .build();

        var o3 = Order.builder()
                .orderId(3)
                .finalAmount(new BigDecimal("200.00"))
                .orderType(Order.OrderType.GENERAL)
                .orderLineItems(List.of("Special Item 1", "Special Item 2"))
                .orderDteTime(LocalDateTime.now())
                .build();

        var o4 = Order.builder()
                .orderId(4)
                .finalAmount(new BigDecimal("75.00"))
                .orderType(Order.OrderType.RESTAURANT)
                .orderLineItems(List.of("Express Item 1"))
                .orderDteTime(LocalDateTime.now())
                .build();

        return Stream.of(o1, o2, o3, o4)
                .map(order -> {
                    try {
                        return mapper.writeValueAsString(order);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    private String key() {
        return String.valueOf(new Random().nextInt(Integer.MAX_VALUE));
    }
}
