package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Coupon;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CouponService {

    List<Coupon> findAll();

    Coupon findById(Long id);

    Coupon save(Coupon entity);

    void deleteById(Long id);
}
