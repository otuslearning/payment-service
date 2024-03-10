package com.example.paymentservice.repository;

import com.example.paymentservice.domain.Payment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Long> {
    Optional<Payment> findByOrderGuid(String orderGuid);
}
