### Kafka Streams Overview

**Topology:**
- **Definition:** A data pipeline or template outlining how data flows through the system.

**Task:**
- **Definition:** A unit of work in a Kafka Stream. The number of tasks is determined by the number of partitions in the source topic. For example, a topic with 4 partitions creates 4 tasks.
- **Benefit:** Tasks enable parallel execution within the application, allowing different tasks to run concurrently on different threads.

**Execution Process:**
1. **Task Creation:** Kafka creates tasks for the topology based on the defined stream processing logic.

2. **Task Execution:** Tasks are executed by stream threads.

3. **Concurrency:** By default, Kafka Streams consumes topics from all partitions, unlike @KafkaListener. For instance, if a topic has 4 partitions and there are 4 replicas of the backend in a Kubernetes (k8s) cluster, each stream consumes messages from all partitions.
