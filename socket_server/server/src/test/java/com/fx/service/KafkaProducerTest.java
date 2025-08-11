package com.fx.service;

import com.fx.service.kafka.KafkaProducerService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DisplayName("Kafka producer")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class KafkaProducerTest {

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
        DockerImageName.parse("apache/kafka-native:3.8.0")
    );

    @Test
    void checkCorrectKafkaConnection() {
        String ipAddress = kafka.getBootstrapServers();

        KafkaProducerService producer = new KafkaProducerService(ipAddress);
        try {
            producer.sendMessage("TestTopic", "Test message");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProperties(ipAddress));
        consumer.subscribe(List.of("TestTopic"));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(20));

        assertEquals(1, records.count());
        assertTrue(records.iterator().hasNext());
        assertEquals("Test message", records.iterator().next().value());
    }

    private Properties consumerProperties(String ipAddress) {
        Properties kafkaProp = new Properties();
        kafkaProp.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ipAddress);
        kafkaProp.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group-id");
        kafkaProp.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        kafkaProp.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        kafkaProp.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return kafkaProp;
    }

    @BeforeAll
    static void initial(TestInfo testInfo, TestReporter testReporter) {
        System.out.println("Running " + testInfo.getDisplayName() + " with tag " + testInfo.getTags());
    }

    @AfterAll
    static void finalizing(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName() + " TEST FINISHED");
    }

}
