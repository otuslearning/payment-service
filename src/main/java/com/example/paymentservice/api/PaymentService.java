package com.example.paymentservice.api;

import com.example.paymentservice.exception.PaymentNotSucceedException;

public interface PaymentService {
    void processPayment(String orderGuid, Integer price) throws PaymentNotSucceedException;
    void processRevertPayment(String orderGuid);
}
