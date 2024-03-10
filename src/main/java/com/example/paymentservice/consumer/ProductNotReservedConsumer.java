package com.example.paymentservice.consumer;

import com.example.paymentservice.api.PaymentService;
import com.example.paymentservice.api.ProductNotReservedEvent;
import com.example.paymentservice.producer.PaymentProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductNotReservedConsumer {

    private final ObjectMapper mapper;
    private final PaymentService paymentService;
    private final PaymentProducer paymentProducer;
    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${kafka.topics.product-not-reserved}")
    public void consume(String message) {
        ProductNotReservedEvent productNotReservedEvent = null;
        try {
            productNotReservedEvent = mapper.readValue(message, ProductNotReservedEvent.class);
            paymentService.processRevertPayment(productNotReservedEvent.getOrderGuid());
        } catch (JsonProcessingException e) {
            log.error("Error parsing message: {}", message, e);
        }
    }
}
