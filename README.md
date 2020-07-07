# kafka-streams-partitioning

Basic example showing how partitioning affects join operations in Kafka Streams.

## Prerequisites

    docker-compose up -d

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

In streaming applications its a common requirement to take as input an stream (for example comming for Change Data Capture producer) and enrich the data with additional information. For example, orders generated in a relational database engine can be enriched with information of the order's product.

Link: https://kafka.apache.org/23/documentation/streams/developer-guide/dsl-api.html#join-co-partitioning-requirements
