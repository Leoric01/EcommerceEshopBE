package com.leoric.ecommerceshopbe.stripe.services;

import com.leoric.ecommerceshopbe.stripe.PaymentOrderRepository;
import com.leoric.ecommerceshopbe.stripe.model.PaymentOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentOrderRepository;

    @Override
    public List<PaymentOrder> findAll() {
        return paymentOrderRepository.findAll();
    }

    @Override
    public PaymentOrder findById(Long id) {
        return paymentOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentOrder not found"));
    }

    @Override
    public PaymentOrder save(PaymentOrder entity) {
        return paymentOrderRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        paymentOrderRepository.deleteById(id);
    }
}