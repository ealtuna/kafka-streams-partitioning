package org.example.ealtuna.kstreamspartitioning.producer;

import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.ealtuna.kstreamspartitioning.model.Order;
import org.example.ealtuna.kstreamspartitioning.model.Product;

import java.util.Properties;

public class AvroProducer {
    public static void main(String[] args) {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        props.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        try (KafkaProducer<String, SpecificRecord> producer = new KafkaProducer(props)) {
            for (int p = 1; p <= 20; p++) {
                Product product = new Product(Integer.toString(p), "product-" + p);
                ProducerRecord<String, SpecificRecord> productRecord = new ProducerRecord("products", product);
                producer.send(productRecord);
                for (int o = 1; o <= 100; o++) {
                    Order order = new Order( Integer.toString(o), product.getId(), p * o + o);
                    ProducerRecord<String, SpecificRecord>  orderRecord = new ProducerRecord("orders", order);
                    producer.send(orderRecord);
                }
                producer.flush();
            }
        } catch (final SerializationException e) {
            e.printStackTrace();
        }
    }
}
