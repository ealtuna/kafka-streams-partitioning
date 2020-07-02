package org.example.ealtuna.kstreamspartitioning.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.example.ealtuna.kstreamspartitioning.model.Order;
import org.example.ealtuna.kstreamspartitioning.model.Product;

import java.util.Properties;

public class SimpleProducer {
    public static void main(String[] args) {
        System.out.println("start");

        Properties properties= new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        KafkaProducer<String, JsonNode> producer = new KafkaProducer(properties);
        ObjectMapper mapper = new ObjectMapper();

        Product product = new Product("1", "product-1");
        ProducerRecord<String, JsonNode> record = new ProducerRecord("first_topic", mapper.valueToTree(product));
        producer.send(record);
        producer.flush();

        Order order = new Order("1", "1", 10);
        record = new ProducerRecord("second_topic", mapper.valueToTree(order));
        producer.send(record);
        producer.flush();

        producer.close();
        System.out.println("end");
    }
}
