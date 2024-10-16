package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.PaymentOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PaymentOrderService {

    List<PaymentOrder> findAll();

    PaymentOrder findById(Long id);

    PaymentOrder save(PaymentOrder entity);

    void deleteById(Long id);
}
