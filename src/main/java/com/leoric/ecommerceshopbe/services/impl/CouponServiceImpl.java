package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Coupon;
import com.leoric.ecommerceshopbe.repositories.CouponRepository;
import com.leoric.ecommerceshopbe.services.interfaces.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon findById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coupon not found"));
    }

    @Override
    public Coupon save(Coupon entity) {
        return couponRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        couponRepository.deleteById(id);
    }
}
