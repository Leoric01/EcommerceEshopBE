package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.CartItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartItemService {

    CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem);

    void removeCartItem(Long userId, Long cartItemId);

    CartItem findCartItemById(Long cartItemId);

    List<CartItem> findAll();

    CartItem findById(Long id);

    CartItem save(CartItem entity);

    void deleteById(Long id);
}
