package com.fx.service.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaProducerService {

    private KafkaProducer<String, String> producer;

    public KafkaProducerService(String ipAddres) {
        Properties kafkaProp = new Properties();
        kafkaProp.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ipAddres);
        kafkaProp.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProp.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProp.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        kafkaProp.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        this.producer = new KafkaProducer<>(kafkaProp);
    }

    public void sendMessage(String topic, String mes) throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, mes);
        producer.send(record).get();
        producer.flush();
    }

}
