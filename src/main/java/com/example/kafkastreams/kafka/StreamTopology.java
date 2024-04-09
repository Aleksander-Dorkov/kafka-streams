package com.example.kafkastreams.kafka;

import com.example.kafkastreams.kafka.dto.Greeting;
import com.example.kafkastreams.kafka.dto.Order;
import com.example.kafkastreams.kafka.serdes.CustomSerdes;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StreamTopology {

    //    StreamsConfig.NUM_STREAM_THREADS_CONFIG - to adjust the amount of threads used in a stream

    // merge topology
    @Autowired
    public void greetingTopology(StreamsBuilder streamsBuilder) {
        KStream<String, Greeting> streamOne = streamsBuilder
                .stream(KafkaTopics.GREETINGS_ONE, Consumed.with(Serdes.String(), CustomSerdes.GREETING));
        KStream<String, Greeting> streamTwo = streamsBuilder
                .stream(KafkaTopics.GREETINGS_TWO, Consumed.with(Serdes.String(), CustomSerdes.GREETING));

        KStream<String, Greeting> mergeStream = streamOne.merge(streamTwo);

        mergeStream
                .peek((k, v) -> System.out.println("GreetingTopology Received message: key=" + k + ", value=" + v))
                .mapValues((k, v) -> new Greeting(v.message(), v.timeStamp()))
                .peek((k, v) -> System.out.println("Stream After Map message: key=" + k + ", value=" + v))
                .to(KafkaTopics.GREETINGS_UPPER_CASE, Produced.with(Serdes.String(), CustomSerdes.GREETING));
    }

    // split topology
    @Autowired
    public void orderTopology(StreamsBuilder streamsBuilder) {
        KStream<String, Order> ordersStream = streamsBuilder
                .stream(KafkaTopics.ORDERS, Consumed.with(Serdes.String(), CustomSerdes.ORDER));
        ordersStream
                .peek((k, v) -> System.out.println("OrderTopology Received message: key=" + k + ", value=" + v))
                .split(Named.as("my-split"))
                .branch((key, value) -> value.orderType() == Order.OrderType.GENERAL,
                        Branched.withConsumer(generalOrdersStream ->
                                generalOrdersStream
                                        .peek((k, v) -> System.out.println("GeneralOrdersBranch Received message: key=" + k + ", value=" + v))
                                        .to(KafkaTopics.GENERAL_ORDERS, Produced.with(Serdes.String(), CustomSerdes.ORDER)))
                )
                .branch((key, value) -> value.orderType() == Order.OrderType.RESTAURANT,
                        Branched.withConsumer(restaurantOrdersStream ->
                                restaurantOrdersStream
                                        .peek((k, v) -> System.out.println("RestaurantOrdersBranch Received message: key=" + k + ", value=" + v))
                                        .to(KafkaTopics.RESTAURANT_ORDERS, Produced.with(Serdes.String(), CustomSerdes.ORDER)))
                );
    }
}
