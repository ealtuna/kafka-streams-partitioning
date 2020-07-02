package org.example.ealtuna.kstreamspartitioning.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.example.ealtuna.kstreamspartitioning.model.Order;
import org.example.ealtuna.kstreamspartitioning.model.Product;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class SimpleConsumer {
    public static void main(String[] args) {
        Properties properties= new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "simpleConsumer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        KafkaConsumer<String, JsonNode> consumer = new KafkaConsumer(properties);

        ObjectMapper mapper = new ObjectMapper();

        consumer.subscribe(Arrays.asList("second_topic"));
        ConsumerRecords<String, JsonNode> records = consumer.poll(10000);
        System.out.println(records.count());
        try {
            for (ConsumerRecord<String, JsonNode> record : records) {
                Order order = mapper.treeToValue(record.value(), Order.class);
                System.out.println(order);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
