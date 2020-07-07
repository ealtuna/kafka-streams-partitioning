package org.example.ealtuna.kstreamspartitioning.kstreams;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;

import org.example.ealtuna.kstreamspartitioning.model.*;

public class OrderProductJoin {

    public static final String PRODUCTS_TOPIC = "products";
    public static final String ORDERS_TOPIC = "orders";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "order-product-join");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        Map<String, String> serdeConfig = Collections.singletonMap(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");

        final SpecificAvroSerde<Product> productValueSerde = new SpecificAvroSerde();
        productValueSerde.configure(serdeConfig, false);

        final SpecificAvroSerde<Product> productKeySerde = new SpecificAvroSerde();
        productKeySerde.configure(serdeConfig, true);

        final SpecificAvroSerde<Order> orderValueSerde = new SpecificAvroSerde();
        orderValueSerde.configure(serdeConfig, false);

        final SpecificAvroSerde<ProductOrder> productOrderValueSerde = new SpecificAvroSerde();
        productOrderValueSerde.configure(serdeConfig, false);

        final SpecificAvroSerde<ProductOrder> productOrderKeySerde = new SpecificAvroSerde();
        productOrderKeySerde.configure(serdeConfig, false);

        final StreamsBuilder builder = new StreamsBuilder();
        KTable<String, Product> products = builder.table(PRODUCTS_TOPIC, Consumed.with(Serdes.String(), productValueSerde));
        KStream<String, Order> orders = builder.stream(ORDERS_TOPIC, Consumed.with(Serdes.String(), orderValueSerde));
        KStream<String, Order> ordersByProductId = orders.selectKey((k, v) -> v.getProductId().toString());
        KStream<String, ProductOrder> productsOrders = ordersByProductId.leftJoin(
                products,
                (order, product) -> new ProductOrder(order.getId(), product.getId(), order.getAmount(), product.getName()),
                Joined.with(Serdes.String(), orderValueSerde, productValueSerde)
        );
        KTable<String, Long> productOrdersCount = productsOrders.groupBy(
                (productId, productOrder) -> productOrder.getProductId().toString(),
                Grouped.with(Serdes.String(), productOrderValueSerde)
        ).count();

        productOrdersCount.toStream().peek((k,v) -> System.out.println("Key: " + k + " - " + v));
        Topology streamTopology = builder.build();

        final KafkaStreams streams = new KafkaStreams(streamTopology, properties);

        streams.cleanUp();
        streams.start();

        // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                streams.close();
            } catch (final Exception e) {
                // ignored
            }
        }));
    }
}
