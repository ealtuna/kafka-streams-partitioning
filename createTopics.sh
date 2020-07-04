docker-compose exec kafka  kafka-topics --create --topic products --partitions 4 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181

docker-compose exec kafka  kafka-topics --create --topic orders --partitions 1 --replication-factor 1 --if-not-exists --zookeeper zookeeper:2181
