### Kafka Streams Overview

**Topology:**

- **Definition:** A data pipeline or template outlining how data flows through the system.

**KTable:**

- Hold the latest value for a given key.
- Any record without key will be ignored
- Use cases: any app that needs to maintain the latest value for a given key

**RocksDB:**

- **Definition:** RocksDB is the default state store used by Kafka Streams.
- **Purpose:** It serves as a local state store for storing and querying stateful data efficiently.

**Task:**

- **Definition:** A unit of work in a Kafka Stream. The number of tasks is determined by the number of partitions in the
  source topic. For example, a topic with 4 partitions creates 4 tasks.
- **Benefit:** Tasks enable parallel execution within the application, allowing different tasks to run concurrently on
  different threads.

**Execution Process:**

1. **Task Creation:** Kafka creates tasks for the topology based on the defined stream processing logic.

2. **Task Execution:** Tasks are executed by stream threads.

3. **Concurrency:** By default, Kafka Streams consumes topics from all partitions, unlike @KafkaListener. For instance,
   if a topic has 4 partitions and there are 4 replicas of the backend in a Kubernetes (k8s) cluster, each stream
   consumes messages from all partitions.
