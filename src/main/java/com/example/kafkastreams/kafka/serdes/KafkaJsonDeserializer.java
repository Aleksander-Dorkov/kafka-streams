package com.example.kafkastreams.kafka.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class KafkaJsonDeserializer<T> implements Deserializer<T> {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());;
    private final Class<T> type;

    public KafkaJsonDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, type);
        } catch (IOException e) {
            throw new RuntimeException("Error deserializing JSON", e);
        }
    }
}
