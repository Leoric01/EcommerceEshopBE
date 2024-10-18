package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.CartItem;
import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    CartItem addCartItem(User user, Product product, String size, int quantity);

    Cart findUserCart(User user);

    List<Cart> findAll();

    Cart findById(Long id);

    Cart save(Cart entity);

    void deleteById(Long id);

}
