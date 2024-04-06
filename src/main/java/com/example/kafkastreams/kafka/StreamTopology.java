package com.example.kafkastreams.kafka;

import com.example.kafkastreams.kafka.dto.Greeting;
import com.example.kafkastreams.kafka.serdes.CustomSerdes;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamTopology {

    @Autowired
    public void process(StreamsBuilder streamsBuilder) {
        KStream<String, Greeting> streamOne = streamsBuilder
                .stream(KafkaTopics.GREETINGS_ONE, Consumed.with(Serdes.String(), CustomSerdes.GREETING));
        KStream<String, Greeting> streamTwo = streamsBuilder
                .stream(KafkaTopics.GREETINGS_TWO, Consumed.with(Serdes.String(), CustomSerdes.GREETING));

        KStream<String, Greeting> mergeStream = streamOne.merge(streamTwo);

        mergeStream.peek((k, v) -> System.out.println("Stream Received message: key=" + k + ", value=" + v))
                .mapValues((k, v) -> new Greeting(v.message(), v.timeStamp()))
                .peek((k, v) -> System.out.println("Stream After Map message: key=" + k + ", value=" + v))
                .to(KafkaTopics.GREETINGS_UPPER_CASE, Produced.with(Serdes.String(), CustomSerdes.GREETING));
    }
}
