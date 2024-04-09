package com.example.kafkastreams.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopics {

    public static final String GREETINGS_ONE = "greetings";
    public static final String GREETINGS_TWO = "greetings_spanish";
    public static final String GREETINGS_UPPER_CASE = "greetings_uppercase";
    public static final String ORDERS = "orders";
    public static final String GENERAL_ORDERS = "general_orders";
    public static final String RESTAURANT_ORDERS = "restaurant_orders";

    @Bean
    public NewTopic topicBuilder() {
        return TopicBuilder.name(GREETINGS_ONE)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicBuilder2() {
        return TopicBuilder.name(GREETINGS_UPPER_CASE)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicBuilder3() {
        return TopicBuilder.name(GREETINGS_TWO)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicBuilder4() {
        return TopicBuilder.name(ORDERS)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicBuilder5() {
        return TopicBuilder.name(GENERAL_ORDERS)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicBuilder6() {
        return TopicBuilder.name(RESTAURANT_ORDERS)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
