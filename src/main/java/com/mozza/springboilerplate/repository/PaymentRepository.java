package com.mozza.springboilerplate.repository;

import com.mozza.springboilerplate.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID>, PaymentRepositoryCustom {
}
