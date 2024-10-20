package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.Wishlist;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.ProductService;
import com.leoric.ecommerceshopbe.services.interfaces.WishlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;
    private final ProductService productService;


    @GetMapping("")
    public ResponseEntity<Result<Wishlist>> getWishlistByUserId(Authentication authentication) {
        Wishlist wishlist = wishlistService.getWishlistByUserId(authentication);
        Result<Wishlist> resp = Result.success(wishlist, "wishlist was fetched or created new one", OK.value());
        return ResponseEntity.status(OK).body(resp);
    }

    @PostMapping("/create")
    public ResponseEntity<Result<Wishlist>> createWishlist(Authentication authentication) {
        Wishlist wishlist = wishlistService.createWishList(authentication);
        Result<Wishlist> resp = Result.success(wishlist, "wishlist created success", CREATED.value());
        return ResponseEntity.status(CREATED).body(resp);
    }

    @PostMapping("/add-product/{productId}")
    public ResponseEntity<Result<Wishlist>> addProductToWishList(Authentication authentication, @PathVariable Long productId) {
        Product product = productService.findProductById(productId);
        Wishlist wishlist = wishlistService.updateProductToWishlist(authentication, product);
        Result<Wishlist> resp = Result.success(wishlist, "wishlist products altered success", CREATED.value());
        return ResponseEntity.status(CREATED).body(resp);
    }
}
