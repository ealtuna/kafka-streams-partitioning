# kafka-streams-partitioning

## Prerequisites

    docker-compose up -d

```
docker-compose exec kafka  \
kafka-topics --create --topic products --partitions 3 \
--replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
```    

```
docker-compose exec kafka  \
kafka-topics --create --topic orders --partitions 3 \
--replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
```

To check the status of the topic:

```
docker-compose exec kafka  \
  kafka-topics --describe --topic orders --zookeeper zookeeper:2181
```

