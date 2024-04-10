### Kafka Streams Overview

**Topology:**

- **Definition:** A data pipeline or template outlining how data flows through the system.
- it is a Directed Acyclic Graph (DAG)
  - Acyclic - mean Non-Cyclical - a graph that does not repeat it self. It means it avoids cycles.
  - Directed - the flow has a defined direction
- It consist of the fallowing
  - Multiple entry nodes Source nodes - like @KafkaListner
  - User processor nodes - our custom logic like `.map()` and `.flter()`
  -  Sink Processor nods - like `kafkaTemplate.send()`

**Task:**

- **Definition:** A unit of work in a Kafka Stream. The number of tasks is determined by the number of partitions in the
  source topic. For example, a topic with 4 partitions creates 4 tasks.
- **Benefit:** Tasks enable parallel execution within the application, allowing different tasks to run concurrently on
  different threads.

**Execution Process:**

1. **Task Creation:** Kafka creates tasks for the `topology` based on the defined stream processing logic.

2. **Task Execution:** Tasks are executed by stream threads.

3. **Concurrency:** By default, Kafka Streams consumes topics from all partitions, unlike @KafkaListener. For instance,
   if a topic has 4 partitions and there are 4 replicas of the backend in a Kubernetes (k8s) cluster, each stream
   consumes messages from all partitions.

**KTable:**

- Hold the latest value for a given key.
- Any record without key will be ignored
- Use cases: any app that needs to maintain the latest value for a given key
- `Materialized.as("my-db-view")` - creates a changelog topic called 'greetings-kafka-stream-my-db-view-changelog' used
  for fault tolerance and state restoration.. This is how get the latest k value after app restart.

**RocksDB:**

- **Definition:** RocksDB is the default state store used by Kafka Streams.
- **Purpose:** It serves as a local state store for storing and querying stateful data efficiently. Provides fault
  tolerance and scalability for stateful operations in Kafka Streams.

**RocksDB and KTable**

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

- Records published to KafkaTopics.WORDS are consumed by your KTable topology.
- Processed records are stored in the local state store (RocksDB) under the name "my-db-view".
- Updates to the state store are also written to the changelog topic 'greetings-kafka-stream-my-db-view-changelog' for
  fault tolerance and state restoration purposes.


stream requires less code the normal kafka without spring boot
