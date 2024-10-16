package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    List<Order> findAll();

    Order findById(Long id);

    Order save(Order entity);

    void deleteById(Long id);
}
