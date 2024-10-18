package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.*;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.*;
import com.leoric.ecommerceshopbe.stripe.constants.PaymentMethod;
import com.leoric.ecommerceshopbe.stripe.dtos.PaymentLinkResponse;
import com.leoric.ecommerceshopbe.stripe.services.PaymentService;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getAccountFromAuthentication;
import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getPrincipalAsUser;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CartService cartService;
    private final PaymentService paymentService;
    private final OrderItemService orderItemService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;

    @PostMapping
    public ResponseEntity<PaymentLinkResponse> createOrder(Authentication authentication,
                                                           @RequestBody Address shippingAddress,
                                                           @RequestParam PaymentMethod paymentMethod
    ) {
        User user = getPrincipalAsUser(authentication);
        Cart cart = cartService.findUserCart(user);
        Set<Order> orders = orderService.createOrder(authentication, shippingAddress, cart);
//        PaymentOrder paymentOrder = paymentService.createOrder(user, shippingAddress, cart);
        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        return new ResponseEntity<>(paymentLinkResponse, OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Result<List<Order>>> usersOrderHistoryHandlers(Authentication authentication) {
        List<Order> orders = orderService.usersOrderHistory(authentication);
        Result<List<Order>> result = Result.success(orders, "Users history of orders", OK.value());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Result<Order>> getOrderById(Authentication authentication, @PathVariable Long orderId) {
        Account account = getAccountFromAuthentication(authentication);
        Order order = orderService.getOrderByIdAndValidateRelation(orderId, account);
        Result<Order> result = Result.success(order, "Order found by id and verified it related to current account",
                OK.value());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<Result<OrderItem>> getOrderItemById(Authentication authentication, @PathVariable Long orderItemId) {
        Account account = getAccountFromAuthentication(authentication);
        OrderItem orderItem = orderService.getOrderItemById(orderItemId);
        Result<OrderItem> result = Result.success(orderItem, "Order item found by id", OK.value());
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Result<Order>> cancelOrder(Authentication authentication, @PathVariable Long orderId) {
        Order order = orderService.cancelOrder(orderId, authentication);
        Seller seller = sellerService.getSellerById(order.getSellerId());
        SellerReport report = sellerReportService.getSellerReport(seller);

        report.setCanceledOrders(report.getCanceledOrders() + 1);
        report.setTotalRefunds(report.getTotalRefunds() + order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);

        Result<Order> result = Result.success(order, "Order successfully canceled", OK.value());
        return ResponseEntity.ok(result);
    }
}
