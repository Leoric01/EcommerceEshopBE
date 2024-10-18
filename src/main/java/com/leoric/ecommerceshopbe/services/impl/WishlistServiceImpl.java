package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Wishlist;
import com.leoric.ecommerceshopbe.repositories.WishlistRepository;
import com.leoric.ecommerceshopbe.services.interfaces.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public List<Wishlist> findAll() {
        return wishlistRepository.findAll();
    }

    @Override
    public Wishlist findById(Long id) {
        return wishlistRepository.findById(id).orElseThrow(() -> new RuntimeException("Wishlist not found"));
    }

    @Override
    public Wishlist save(Wishlist entity) {
        return wishlistRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        wishlistRepository.deleteById(id);
    }
}
