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

        for (int p = 1; p <= 20; p++) {
            Product product = new Product(Integer.toString(p), "product-" + p);
            ProducerRecord<String, JsonNode> productRecord = new ProducerRecord("products", mapper.valueToTree(product));
            producer.send(productRecord);
            for (int o = 1; o <= 100; o++) {
                Order order = new Order( Integer.toString(o), product.getId(), p * o + o);
                ProducerRecord<String, JsonNode>  orderRecord = new ProducerRecord("orders", mapper.valueToTree(order));
                producer.send(orderRecord);
            }
        }

        producer.flush();
        producer.close();
        System.out.println("end");
    }
}
