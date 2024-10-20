package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.Coupon;
import com.leoric.ecommerceshopbe.security.auth.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CouponService {

    Cart applyCoupon(String code, double orderValue, User user);

    Cart removeCoupon(String code, User user);

    Coupon findCouponByid(Long id);

    Coupon createCoupon(Coupon coupon);

    List<Coupon> findAllCoupons();

    void deleteCouponById(Long couponId);

    List<Coupon> findAll();

    Coupon findById(Long id);

    Coupon save(Coupon entity);
}