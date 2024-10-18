package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Order;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.constants.OrderStatus;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.OrderService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/order")
public class SellerOrderController {
    private final OrderService orderService;

    @GetMapping("")
    public ResponseEntity<Result<List<Order>>> getAllOrders(Authentication authentication) {
        Seller seller = GlobalUtil.getPrincipalAsSeller(authentication);
        List<Order> orders = orderService.sellersOrders(seller.getId());
        Result<List<Order>> result = Result.success(orders, "Sellers orders fetched succesfully", ACCEPTED.value());
        return ResponseEntity.status(ACCEPTED).body(result);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Result<Order>> updateOrderStatus(Authentication authentication, @PathVariable("orderId") Long orderId, @PathVariable OrderStatus orderStatus) {
        //TODO i need to impl check if the order belongs to the seller to be able to update it
        Order order = orderService.updateOrderStatus(orderId, orderStatus);
        Result<Order> response = Result.success(order, "Order status was successfully updated", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

}
