package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.handler.OperationNotPermittedException;
import com.leoric.ecommerceshopbe.models.CartItem;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.CartItemRepository;
import com.leoric.ecommerceshopbe.services.interfaces.CartItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartitemRepository;

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, CartItem cartItem) {
        CartItem item = findCartItemById(cartItemId);
        User cartItemUser = item.getCart().getUser();

        if (cartItemUser.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setMrpPrice(item.getQuantity() * item.getProduct().getMrpPrice());
            item.setSellingPrice(item.getQuantity() * item.getProduct().getSellingPrice());
            return cartitemRepository.save(item);
        }
        throw new OperationNotPermittedException("you do not have permission to update this cart item");
    }


    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem item = findCartItemById(cartItemId);
        User cartItemUser = item.getCart().getUser();
        if (cartItemUser.getId().equals(userId)) {
            cartitemRepository.delete(item);
            return;
        }
        throw new OperationNotPermittedException("you do not have permission to delete this cart item");
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) {
        return cartitemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem by id not found"));
    }

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
