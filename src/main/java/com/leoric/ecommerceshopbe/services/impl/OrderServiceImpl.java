package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.*;
import com.leoric.ecommerceshopbe.models.constants.OrderStatus;
import com.leoric.ecommerceshopbe.repositories.AddressRepository;
import com.leoric.ecommerceshopbe.repositories.OrderItemRepository;
import com.leoric.ecommerceshopbe.repositories.OrderRepository;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.services.interfaces.OrderService;
import com.leoric.ecommerceshopbe.stripe.constants.PaymentStatus;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getPrincipalAsUser;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final AddressRepository addressRepository;
    private final OrderItemRepository orderItemRepository;
    private final SellerRepository sellerRepository;

    @Override
    @Transactional
    public Set<Order> createOrder(Authentication connectedUser, Address shippingAddress, Cart cart) {
        if (shippingAddress == null || cart == null) {
            throw new IllegalArgumentException("Invalid input: User, shipping address, or cart is null");
        }
        User user = getPrincipalAsUser(connectedUser);
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

            saveOrderItems(savedOrder, items);
        }
        return orders;
    }


    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order Not Found"));
    }

    @Override
    public List<Order> usersOrderHistory(Authentication connectedUser) {
        User user = getPrincipalAsUser(connectedUser);
        return orderRepository.findAllByUserId(user.getId());
    }

    @Override
    public List<Order> sellersOrders(Long sellerId) {
        if (sellerId == null || !sellerRepository.existsById(sellerId)) {
            throw new IllegalArgumentException("Invalid input: sellerId is null or non-existent");
        }
        return orderRepository.findAllBySellerId(sellerId);
    }

    @Override
    public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Order order = findOrderById(orderId);
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId, Authentication connectedUser) {
        User user = getPrincipalAsUser(connectedUser);
        Order order = findOrderById(orderId);
        if (!user.getId().equals(order.getUser().getId())) {
            throw new BadCredentialsException("Access denied: the order belongs to a different user account");
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    public Order save(Order entity) {
        return orderRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("OrderItem Not Found"));
    }

    @Override
    public Order getOrderByIdAndValidateRelation(Long orderId, Account account) {
        Order order = findOrderById(orderId);
        Long userId = Optional.ofNullable(order.getUser())
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("Order has no associated user"));

        Long sellerId = Optional.ofNullable(order.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("Order has no associated seller"));

        if (!account.getId().equals(userId) && !account.getId().equals(sellerId)) {
            throw new IllegalArgumentException("This order does not belong to this account");
        }

        return order;
    }

    private void saveOrderItems(Order savedOrder, List<CartItem> items) {
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
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);
    }
}
