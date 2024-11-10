package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.handler.OperationNotPermittedException;
import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.Coupon;
import com.leoric.ecommerceshopbe.models.constants.CouponStatus;
import com.leoric.ecommerceshopbe.repositories.CartRepository;
import com.leoric.ecommerceshopbe.repositories.CouponRepository;
import com.leoric.ecommerceshopbe.requests.dto.CouponRequestDto;
import com.leoric.ecommerceshopbe.response.CouponDtoResponse;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.services.interfaces.CartService;
import com.leoric.ecommerceshopbe.services.interfaces.CouponService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    @Override
    public Cart applyCoupon(String code, double orderValue, User user) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found by code"));
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found by user id"));
        if (user.getUsedCoupons().contains(coupon)) {
            throw new OperationNotPermittedException("Coupon already used");
        }
        if (orderValue < coupon.getMinimumOrderValue()) {
            throw new OperationNotPermittedException("Cannot apply this coupon, order value is less than the minimum order value");
        }
        if (coupon.isActive() && LocalDate.now().isAfter(coupon.getValidityStartDate())
            && LocalDate.now().isBefore(coupon.getValidityEndDate())) {
            user.getUsedCoupons().add(coupon);
            userRepository.save(user);
            if (cart.getPreDiscountPrice() == 0) {
                cart.setPreDiscountPrice(cart.getTotalSellingPrice());
            }
            double discountedPrice = cart.getPreDiscountPrice() * coupon.getDiscountPercentage() / 100;
            cart.setTotalSellingPrice(cart.getPreDiscountPrice() - discountedPrice);
            cart.setCouponCode(code);
            cartRepository.save(cart);
            return cart;
        }

        throw new OperationNotPermittedException("Coupon does not meet the conditions to be applied");
    }

    @Override
    public Cart removeCoupon(String code, User user) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found by code"));

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found by user id"));
        cart.setTotalSellingPrice(cart.getPreDiscountPrice());
        cart.setPreDiscountPrice(0);
        user.getUsedCoupons().remove(coupon);
        cart.setCouponCode(null);

        userRepository.save(user);
        return cartRepository.save(cart);
    }

    @Override
    public Coupon findCouponByid(Long id) {
        return couponRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Coupon createCoupon(CouponRequestDto couponReq) {
        Coupon coupon = new Coupon();
        coupon.setCode(couponReq.getCode());
        coupon.setDiscountPercentage(couponReq.getDiscountPercentage());
        coupon.setValidityStartDate(couponReq.getValidityStartDate());
        coupon.setValidityEndDate(couponReq.getValidityEndDate());
        coupon.setMinimumOrderValue(couponReq.getMinimumOrderValue());
        return couponRepository.save(coupon);
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public List<Coupon> findAllCoupons() {
        return couponRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole ('ADMIN')")
    public void deleteCouponById(Long couponId) {
        if (couponRepository.existsById(couponId)) {
            couponRepository.deleteById(couponId);
            return;
        }
        throw new EntityNotFoundException("Coupon not found by id");
    }

    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public Coupon findById(Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Coupon not found"));
    }

    @Override
    public Coupon save(Coupon entity) {
        return couponRepository.save(entity);
    }

    @Override
    public List<CouponDtoResponse> getAllCouponsAsDtos() {
        return couponRepository.findAll().stream()
                .filter(Objects::nonNull)
                .map(coupon -> {
                    CouponDtoResponse couponDtoResponse = new CouponDtoResponse();
                    couponDtoResponse.setId(coupon.getId());
                    couponDtoResponse.setCode(coupon.getCode());
                    couponDtoResponse.setDiscountPercentage(coupon.getDiscountPercentage());
                    couponDtoResponse.setValidityStartDate(coupon.getValidityStartDate());
                    couponDtoResponse.setValidityEndDate(coupon.getValidityEndDate());
                    couponDtoResponse.setMinimumOrderValue(coupon.getMinimumOrderValue());
                    couponDtoResponse.setStatus(findCorrectStatus(coupon));
                    return couponDtoResponse;
                }).collect(Collectors.toList());
    }

    @Override
    public Coupon updateCouponStatus(Long couponId, String status) {
        if (couponId == null || status == null || status.isBlank()) {
            throw new IllegalArgumentException("Coupon id and status are required both");
        }
        if (status.equalsIgnoreCase(CouponStatus.SUSPENDED.name())) {
            Coupon coupon = findCouponByid(couponId);
            if (coupon.isActive()) {
                coupon.setActive(false);
            }
            return save(coupon);

        } else if (status.equalsIgnoreCase(CouponStatus.ACTIVE.name())) {
            Coupon coupon = findCouponByid(couponId);
            if (!coupon.isActive()) {
                coupon.setActive(true);
            }
            return save(coupon);
        }
        throw new OperationNotPermittedException("Coupon update params does not meet the conditions");
    }

    private String findCorrectStatus(Coupon coupon) {
        if (coupon == null) {
            throw new IllegalArgumentException("Coupon object is null");
        }
        if (coupon.getValidityStartDate() == null) {
            throw new IllegalArgumentException("Coupon validity start date is null");
        }
        if (coupon.getValidityEndDate() == null) {
            throw new IllegalArgumentException("Coupon validity end date is null");
        }
        if (!coupon.isActive()) {
            return CouponStatus.SUSPENDED.name();
        } else if (coupon.getValidityStartDate().isAfter(coupon.getValidityEndDate())) {
            return CouponStatus.INVALID.name();
        } else if (coupon.getValidityStartDate().isAfter(LocalDate.now())) {
            return CouponStatus.NOT_ACTIVE_YET.name();
        } else if (coupon.getValidityEndDate().isBefore(LocalDate.now())) {
            return CouponStatus.EXPIRED.name();
        } else if (LocalDate.now().isBefore(coupon.getValidityEndDate()) && LocalDate.now().isAfter(coupon.getValidityStartDate())) {
            return CouponStatus.ACTIVE.name();
        } else {
            throw new IllegalStateException("Invalid coupon status");
        }
    }
}