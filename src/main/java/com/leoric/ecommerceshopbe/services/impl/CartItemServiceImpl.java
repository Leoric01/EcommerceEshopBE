package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.CartItem;
import com.leoric.ecommerceshopbe.repositories.CartItemRepository;
import com.leoric.ecommerceshopbe.services.interfaces.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartitemRepository;

    @Override
    public List<CartItem> findAll() {
        return cartitemRepository.findAll();
    }

    @Override
    public CartItem findById(Long id) {
        return cartitemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));
    }

    @Override
    public CartItem save(CartItem entity) {
        return cartitemRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        cartitemRepository.deleteById(id);
    }
}
