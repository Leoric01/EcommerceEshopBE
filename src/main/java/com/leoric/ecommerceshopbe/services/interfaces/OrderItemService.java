package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderItemService {

    List<OrderItem> findAll();

    OrderItem findById(Long id);

    OrderItem save(OrderItem entity);

    void deleteById(Long id);
}
