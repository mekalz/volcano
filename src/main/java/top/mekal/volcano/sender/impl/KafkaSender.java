package top.mekal.volcano.sender.impl;

import top.mekal.volcano.sender.ISender;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;

/**
 * Created by mekal on 28/12/2016.
 */
public class KafkaSender implements ISender {

    public String brokers;
    public String topic;
    public String clientId;

    private Producer<String, String> producer;

    public KafkaSender(String topic, String brokers, String clientId) {
        this.brokers = brokers;
        this.topic = topic;
        this.clientId = clientId;

        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("client.id", clientId);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(props);
    }

    @Override
    public void close() {
        this.producer.close();
        System.out.println("Kafka sender closed.");
    }

    @Override
    public void send(String message) {
        this.producer.send(new ProducerRecord<String, String>(this.topic,  message));
    }
}
