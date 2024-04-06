package com.example.kafkastreams.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StreamTopology {

    @Autowired
    public void process(StreamsBuilder streamsBuilder) {
        KStream<String, String> streamOne = streamsBuilder
                .stream(KafkaTopics.GREETINGS_ONE, Consumed.with(Serdes.String(), Serdes.String()));
        KStream<String, String> streamTwo = streamsBuilder
                .stream(KafkaTopics.GREETINGS_TWO, Consumed.with(Serdes.String(), Serdes.String()));

        KStream<String, String> mergeStream = streamOne.merge(streamTwo);

        mergeStream.peek((k, v) -> System.out.println("Stream Received message: key=" + k + ", value=" + v))
                .filter((k, v) -> {
                    char c = v.strip().charAt(v.length() - 1);
                    return Character.isDigit(c) ? Integer.parseInt(String.valueOf(c)) % 2 == 0 : true;
                })
                .mapValues((k, v) -> v.toUpperCase())
                .peek((k, v) -> System.out.println("Stream After Map And Filter message: key=" + k + ", value=" + v))
                .flatMap((key, value) -> {
                    var newValue = Arrays.asList(value.split(" "));
                    return newValue.stream().map(t -> KeyValue.pair(key, t)).toList();
                })
                .peek((k, v) -> System.out.println("Stream After FlatMap message: key=" + k + ", value=" + v))
                .to(KafkaTopics.GREETINGS_UPPER_CASE, Produced.with(Serdes.String(), Serdes.String()));
    }
}
