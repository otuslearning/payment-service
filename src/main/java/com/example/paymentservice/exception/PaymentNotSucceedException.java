package com.example.paymentservice.exception;

public class PaymentNotSucceedException extends RuntimeException {
    public PaymentNotSucceedException(String message) {
        super(message);
    }
}
