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
