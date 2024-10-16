package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.OrderItem;
import com.leoric.ecommerceshopbe.repositories.OrderItemRepository;
import com.leoric.ecommerceshopbe.services.interfaces.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderitemRepository;

    @Override
    public List<OrderItem> findAll() {
        return orderitemRepository.findAll();
    }

    @Override
    public OrderItem findById(Long id) {
        return orderitemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));
    }

    @Override
    public OrderItem save(OrderItem entity) {
        return orderitemRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        orderitemRepository.deleteById(id);
    }
}
