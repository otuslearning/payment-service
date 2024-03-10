package com.example.paymentservice.service;

import com.example.paymentservice.api.PaymentService;
import com.example.paymentservice.domain.Payment;
import com.example.paymentservice.exception.PaymentNotSucceedException;
import com.example.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private static final String PRODUCT_DUPLICATE_CREATE_REQUEST = "Product already added, requestGuid: %s";
    private static final String PAYMENT_NOT_SUCCEED = "Payment for order guid: %s not succeed";


    @Override
    @Transactional
    public void processPayment(String orderGuid, Integer price) {
        Payment payment = createPayment(orderGuid, price);
        payment.setStatus("SUCCESS");
        try {
            paymentRepository.save(payment);
        } catch (Exception ex) {
            throw new PaymentNotSucceedException(
                    String.format(PAYMENT_NOT_SUCCEED, orderGuid)
            );
        }
    }

    @Override
    @Transactional
    public void processRevertPayment(String orderGuid) {
        Payment payment = paymentRepository.findByOrderGuid(orderGuid).orElseThrow();
        payment.setStatus("PAYMENT_REVERTED");
    }

    private Payment createPayment(String orderGuid, Integer price) {
        Payment payment = new Payment();
        payment.setOrderGuid(orderGuid);
        payment.setPrice(price);
        return payment;
    }
}
