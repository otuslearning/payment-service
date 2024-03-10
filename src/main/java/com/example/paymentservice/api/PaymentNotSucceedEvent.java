package com.example.paymentservice.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentNotSucceedEvent {
    private String orderGuid;
}
