package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.PaymentOrder;
import com.leoric.ecommerceshopbe.repositories.PaymentOrderRepository;
import com.leoric.ecommerceshopbe.services.interfaces.PaymentOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentOrderServiceImpl implements PaymentOrderService {

    private final PaymentOrderRepository paymentorderRepository;

    @Override
    public List<PaymentOrder> findAll() {
        return paymentorderRepository.findAll();
    }

    @Override
    public PaymentOrder findById(Long id) {
        return paymentorderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PaymentOrder not found"));
    }

    @Override
    public PaymentOrder save(PaymentOrder entity) {
        return paymentorderRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        paymentorderRepository.deleteById(id);
    }
}
