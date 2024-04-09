package com.example.kafkastreams.kafka.serdes;

import com.example.kafkastreams.kafka.dto.Greeting;
import com.example.kafkastreams.kafka.dto.Order;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class CustomSerdes {

    public static final Serde<Greeting> GREETING = new Serde<>() {
        @Override
        public Serializer<Greeting> serializer() {
            return new KafkaJsonSerializer<>();
        }

        @Override
        public Deserializer<Greeting> deserializer() {
            return new KafkaJsonDeserializer<>(Greeting.class);
        }
    };

    public static final Serde<Order> ORDER = new Serde<>() {
        @Override
        public Serializer<Order> serializer() {
            return new KafkaJsonSerializer<>();
        }

        @Override
        public Deserializer<Order> deserializer() {
            return new KafkaJsonDeserializer<>(Order.class);
        }
    };
}
