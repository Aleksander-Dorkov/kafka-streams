package com.example.kafkastreams.kafka;

import com.example.kafkastreams.kafka.dto.Greeting;
import com.example.kafkastreams.kafka.dto.Order;
import com.example.kafkastreams.kafka.serdes.CustomSerdes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StreamTopology {

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

    // KTable topology
    @Autowired
    public void kTableTopology(StreamsBuilder streamsBuilder) {
        KTable<String, String> table = streamsBuilder
                .table(KafkaTopics.WORDS,
                        Consumed.with(Serdes.String(), Serdes.String()),
                        Materialized.as("my-db-view")
                );
        table
                .filter((k, v) -> v.length() > 2)
                .toStream()
                .peek((k, v) -> System.out.println("kTableTopology Received message: key=" + k + ", value=" + v));
    }

    // groupBy - count topology
    //after app restart it would have saved the total count of msgs
    @Autowired
    public void aggregateCountTopology(StreamsBuilder streamsBuilder) {
        KStream<String, String> countStream = streamsBuilder
                .stream(KafkaTopics.AGGREGATE_COUNT, Consumed.with(Serdes.String(), Serdes.String()));

        countStream
                .peek((k, v) -> System.out.println("countStream Received message: key=" + k + ", value=" + v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                .count(Named.as("count-per-alphabet"))
                .toStream()
                .peek((k, v) -> System.out.println("count-per-alphabet Received message: key=" + k + ", value=" + v)); // the v is the count of all msgs with the same key
    }

    @Autowired
    public void aggregateReduceTopology(StreamsBuilder streamsBuilder) {
        KStream<String, String> reduceStream = streamsBuilder
                .stream(KafkaTopics.AGGREGATE_REDUCE, Consumed.with(Serdes.String(), Serdes.String()));

        reduceStream
                .peek((k, v) -> System.out.println("reduceStream Received message: key=" + k + ", value=" + v))
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                .reduce((v1, v2) -> v1 + "-" + v2)
                .toStream()
                .peek((k, v) -> System.out.println("after reduce message: key=" + k + ", value=" + v)); // after reduce message: key=a, value=1-2-3-4-5
    }
}
