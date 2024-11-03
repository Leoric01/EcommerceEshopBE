package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.CartItem;
import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.repositories.CartItemRepository;
import com.leoric.ecommerceshopbe.repositories.CartRepository;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.services.interfaces.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = findUserCart(user);
        CartItem isPresent = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());
            cartItem.setSize(size);
            cartItem.setCart(cart);
            int total = product.getSellingPrice() * quantity;
            cartItem.setSellingPrice(total);
            cartItem.setMaxPrice(quantity * product.getSellingPrice());
            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);
            return cartItemRepository.save(cartItem);
        }
        return isPresent;
    }

    @Override
    @Transactional
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setTotalMaxPrice(0);
            newCart.setTotalItem(0);
            newCart.setTotalSellingPrice(0);
            newCart.setDiscount(0);
            return cartRepository.save(newCart);
        });
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getMaxPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalMaxPrice(totalPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountedPrice));

        return cart;
    }

    private int calculateDiscountPercentage(int maxPrice, int sellingPrice) {
        if (maxPrice == 0) {
            return 0;
        }
        if (maxPrice < 0 || sellingPrice < 0) {
            throw new IllegalArgumentException("MaxPrice or sellingPrice are negative");
        }
        return ((maxPrice - sellingPrice) * 100) / maxPrice;
    }

    @Override
    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart findById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public Cart save(Cart entity) {
        return cartRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }
}