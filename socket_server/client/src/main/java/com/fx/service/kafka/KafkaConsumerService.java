package com.fx.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;

public class KafkaConsumerService extends Thread {
    private KafkaConsumer<String, String> consumer;

    public KafkaConsumerService(int id) {
        Properties kafkaProp = createKafkaProperties();
        this.consumer = new KafkaConsumer<>(kafkaProp);
        List<String> topicList = List.of(String.valueOf(id));
        consumer.subscribe(topicList);
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, String> recordsKafka = consumer.poll(10000);
            for (var record : recordsKafka) {
                System.out.println(record.offset() + " " + record.value());
            }
        }
    }

    private Properties createKafkaProperties() {
        Properties kafkaProp = new Properties();
        kafkaProp.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        kafkaProp.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group-id");
        kafkaProp.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProp.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return kafkaProp;
    }
}
