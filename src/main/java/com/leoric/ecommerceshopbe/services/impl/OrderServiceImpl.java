package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.*;
import com.leoric.ecommerceshopbe.models.constants.OrderStatus;
import com.leoric.ecommerceshopbe.models.constants.PaymentStatus;
import com.leoric.ecommerceshopbe.repositories.AddressRepository;
import com.leoric.ecommerceshopbe.repositories.OrderItemRepository;
import com.leoric.ecommerceshopbe.repositories.OrderRepository;
import com.leoric.ecommerceshopbe.services.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Set<Order> createOrder(Authentication connectedUser, Address shippingAddress, Cart cart) {
        User user = (User) connectedUser.getPrincipal();
        user.getAddresses().add(shippingAddress);
        Address address = addressRepository.save(shippingAddress);
        Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
                .collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));
        Set<Order> orders = new HashSet<>();
        for (Map.Entry<Long, List<CartItem>> entry : itemsBySeller.entrySet()) {
            Long sellerId = entry.getKey();
            List<CartItem> items = entry.getValue();
            int totalOrderPrice = items.stream().mapToInt(CartItem::getSellingPrice).sum();
            int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();
            Order createdOrder = new Order();
            createdOrder.setUser(user);
            createdOrder.setSellerId(sellerId);
            createdOrder.setTotalMrpPrice(totalOrderPrice);
            createdOrder.setTotalSellingPrice(totalOrderPrice);
            createdOrder.setTotalItem(totalItem);
            createdOrder.setShippingAddress(address);
            createdOrder.setOrderStatus(OrderStatus.PENDING);
            createdOrder.setPaymentStatus(PaymentStatus.PENDING);
            Order savedOrder = orderRepository.save(createdOrder);
            orders.add(savedOrder);
            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem item : items) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(savedOrder);
                orderItem.setMrpPrice(item.getMrpPrice());
                orderItem.setProduct(item.getProduct());
                orderItem.setQuantity(item.getQuantity());
                orderItem.setSize(item.getSize());
                orderItem.setUserId(item.getUserId());
                orderItem.setSellingPrice(item.getSellingPrice());

                savedOrder.getOrderItems().add(orderItem);

                OrderItem savedOrderItem = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItem);
            }

        }


        return orders;
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
