package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Wishlist;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WishlistService {

    List<Wishlist> findAll();

    Wishlist findById(Long id);

    Wishlist save(Wishlist entity);

    void deleteById(Long id);
}
