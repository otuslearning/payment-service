package com.example.paymentservice.api;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateEvent {
    private String orderGuid;
    private Integer price;
}
