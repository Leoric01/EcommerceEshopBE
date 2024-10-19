package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.models.Wishlist;
import com.leoric.ecommerceshopbe.repositories.WishlistRepository;
import com.leoric.ecommerceshopbe.services.interfaces.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getPrincipalAsUser;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;

    @Override
    public Wishlist createWishList(Authentication authentication) {
        User user = getPrincipalAsUser(authentication);
        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist getWishlistByUserId(Authentication authentication) {
        User user = getPrincipalAsUser(authentication);
        Optional<Wishlist> wishlistOpt = wishlistRepository.findByUserId(user.getId());
        return wishlistOpt.orElseGet(() -> createWishList(authentication));
    }


    @Override
    public Wishlist updateProductToWishlist(Authentication authentication, Product product) {
        Wishlist wishlist = getWishlistByUserId(authentication);
        if (wishlist.getProducts().contains(product)) {
            wishlist.getProducts().remove(product);
        } else {
            wishlist.getProducts().add(product);
        }
        return wishlistRepository.save(wishlist);
    }

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
