package com.example.kafkastreams.kafka.serdes;

import com.example.kafkastreams.kafka.dto.Greeting;
import com.example.kafkastreams.kafka.dto.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Serde;

import static org.apache.kafka.common.serialization.Serdes.serdeFrom;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomSerdes {


    public static final Serde<Greeting> GREETING = serdeFrom(new KafkaJsonSerializer<>(), new KafkaJsonDeserializer<>(Greeting.class));
    public static final Serde<Order> ORDER = serdeFrom(new KafkaJsonSerializer<>(), new KafkaJsonDeserializer<>(Order.class));
}
