package com.example.paymentservice.producer;

import com.example.paymentservice.api.PaymentNotSucceedEvent;
import com.example.paymentservice.api.PaymentSucceedEvent;
import com.example.paymentservice.exception.ConvertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {
    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void messagePaymentSucceed(PaymentSucceedEvent message) {
        Assert.notNull(message, "message mustn't be null");
        try {
            kafkaTemplate.send("payment-succeed", mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("Error convert and send payment succeed event: {}", message, e);
            throw new ConvertException(e.getMessage());
        }
    }

    public void messagePaymentNotSucceed(PaymentNotSucceedEvent message) {
        Assert.notNull(message, "message mustn't be null");
        try {
            kafkaTemplate.send("payment-not-succeed", mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("Error convert and send payment not succeed event: {}", message, e);
            throw new ConvertException(e.getMessage());
        }
    }
}
