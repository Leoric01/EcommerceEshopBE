package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.constants.OrderStatus;
import com.leoric.ecommerceshopbe.repositories.OrderRepository;
import com.leoric.ecommerceshopbe.services.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;


    @Override
    public Set<Order> createOrder(Authentication connectedUser, Address address, Cart cart) {
        return Set.of();
    }

    @Override
    public Order findOrderById(Long id) {
        return null;
    }

    @Override
    public List<Order> usersOrderHistory(Authentication connectedUser) {
        return List.of();
    }

    @Override
    public List<Order> sellersOrder(Long sellerId) {
        return List.of();
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        return null;
    }

    @Override
    public Order cancelOrder(Long orderId, Authentication connectedUser) {
        return null;
    }

    @Override
    public Order save(Order entity) {
        return orderRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
