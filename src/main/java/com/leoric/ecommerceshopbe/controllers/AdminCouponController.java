package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.Coupon;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.services.interfaces.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getPrincipalAsUser;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class AdminCouponController {
    private final CouponService couponService;

    @GetMapping("/admin/all")
    public ResponseEntity<Result<List<Coupon>>> getAllCoupons() {
        List<Coupon> couponList = couponService.findAll();
        Result<List<Coupon>> response = Result.success(couponList, "List of coupons retrieved", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/apply")
    public ResponseEntity<Result<Cart>> applyCoupon(@RequestParam String apply,
                                                    @RequestParam String code,
                                                    @RequestParam double orderValue,
                                                    Authentication authentication) {
        User user = getPrincipalAsUser(authentication);
        Cart cart;
        if (apply.equals("true")) {
            cart = couponService.applyCoupon(code, orderValue, user);
        } else {
            cart = couponService.removeCoupon(code, user);
        }
        Result<Cart> response = Result.success(cart, "Coupon successfully applied", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<Result<Coupon>> createCoupon(@RequestBody Coupon coupon) {
        Coupon createdCoupon = couponService.createCoupon(coupon);
        Result<Coupon> response = Result.success(createdCoupon, "Coupon successfully created", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/delete/{couponId}")
    public ResponseEntity<Result<Void>> deleteCoupon(@PathVariable Long couponId) {
        couponService.deleteCouponById(couponId);
        Result<Void> response = Result.success("Coupon successfully deleted", NO_CONTENT.value());
        return ResponseEntity.status(NO_CONTENT).body(response);
    }

}