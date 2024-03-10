package com.example.paymentservice.consumer;

import com.example.paymentservice.api.PaymentNotSucceedEvent;
import com.example.paymentservice.api.OrderCreateEvent;
import com.example.paymentservice.api.PaymentService;
import com.example.paymentservice.api.PaymentSucceedEvent;
import com.example.paymentservice.exception.PaymentNotSucceedException;
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
public class OrderCreatedConsumer {

    private final ObjectMapper mapper;
    private final PaymentService paymentService;
    private final PaymentProducer paymentProducer;
    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${kafka.topics.order-created}")
    public void consume(String message) {
        OrderCreateEvent orderCreateEvent = null;
        try {
            orderCreateEvent = mapper.readValue(message, OrderCreateEvent.class);
            paymentService.processPayment(orderCreateEvent.getOrderGuid(), orderCreateEvent.getPrice());
            PaymentSucceedEvent succeedPayment = PaymentSucceedEvent.builder()
                    .orderGuid(orderCreateEvent.getOrderGuid())
                    .build();
            paymentProducer.messagePaymentSucceed(succeedPayment);
        } catch (JsonProcessingException e) {
            log.error("Error parsing message: {}", message, e);
        } catch (PaymentNotSucceedException e) {
            log.error("Error processing message: {}", message, e);
            if(orderCreateEvent != null) {
                PaymentNotSucceedEvent notSucceedPaymentEvent = PaymentNotSucceedEvent.builder()
                        .orderGuid(orderCreateEvent.getOrderGuid())
                        .build();
                paymentProducer.messagePaymentNotSucceed(notSucceedPaymentEvent);
            }
        }

    }
}
