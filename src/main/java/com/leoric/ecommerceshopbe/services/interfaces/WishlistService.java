package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.Wishlist;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WishlistService {

    Wishlist getWishlistByUserId(Authentication authentication);

    Wishlist createWishList(Authentication authentication);

    Wishlist updateProductToWishlist(Authentication authentication, Product product);

    List<Wishlist> findAll();

    Wishlist findById(Long id);

    Wishlist save(Wishlist entity);

    void deleteById(Long id);
}
