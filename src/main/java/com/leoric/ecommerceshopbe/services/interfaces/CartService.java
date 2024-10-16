package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Cart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {

    List<Cart> findAll();

    Cart findById(Long id);

    Cart save(Cart entity);

    void deleteById(Long id);
}
