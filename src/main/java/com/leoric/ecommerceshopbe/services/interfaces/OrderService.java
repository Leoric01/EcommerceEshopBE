package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.constants.OrderStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface OrderService {
    Set<Order> createOrder(Authentication connectedUser, Address address, Cart cart);

    Order findOrderById(Long id);

    List<Order> usersOrderHistory(Authentication connectedUser);

    List<Order> sellersOrder(Long sellerId);

    Order updateOrderStatus(Long orderId, OrderStatus orderStatus);

    Order cancelOrder(Long orderId, Authentication connectedUser);

    Order save(Order entity);

    void deleteById(Long id);
}
