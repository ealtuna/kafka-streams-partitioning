# kafka-streams-partitioning

Basic example showing how partitioning affects join operations in Kafka Streams.

## Prerequisites

Install Docker(>1.11) and Docker Compose.

Start Zookeeper, Kafka, and Confluent Schema Registry:

    docker-compose up -d

Create the topics used by this demo:

```
docker-compose exec kafka  \
kafka-topics --create --topic products --partitions 3 \
--replication-factor 1 --if-not-exists --zookeeper zookeeper:2181

docker-compose exec kafka  \
kafka-topics --create --topic orders --partitions 3 \
--replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
```

To check the status of the topics:

```
docker-compose exec kafka kafka-topics --list --zookeeper zookeeper:2181
docker-compose exec kafka kafka-topics --describe --topic products --zookeeper zookeeper:2181
docker-compose exec kafka kafka-topics --describe --topic orders --zookeeper zookeeper:2181
```

## Produce example messages

Use the Avro Producer to generate messages for Products and Orders.

## Partitioning requirements

In streaming applications its a common requirement to take as input an stream (for example comming for Change Data Capture producer) and enrich the data with additional information. For example, orders generated in a relational database engine can be enriched with information of the order's product. The advantage of using the stream driven approach in the enrichment is the possibility to perform lookups at very large scale and with a low processing latency.



Link: https://kafka.apache.org/23/documentation/streams/developer-guide/dsl-api.html#join-co-partitioning-requirements

## References

https://assets.confluent.io/m/7a91acf41502a75e/original/20180328-EB-Confluent_Designing_Event_Driven_Systems.pdf

## Cleanup

Stop the Schema Registry, the Kafka broker, and ZooKeeper instance:

    docker-compose down
