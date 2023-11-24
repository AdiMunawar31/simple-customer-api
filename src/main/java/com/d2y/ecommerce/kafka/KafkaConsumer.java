package com.d2y.ecommerce.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {

  @KafkaListener(topics = "customer-topic", groupId = "customer-group")
  public void consumeMessage(String message) {
    log.info("Data Listener From Kafka : {}", message);
  }
}
