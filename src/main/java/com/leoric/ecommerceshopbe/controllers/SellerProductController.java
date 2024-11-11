package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.requests.dto.CreateProductReqDto;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/seller/products")
public class SellerProductController {
    private final ProductService productService;

    @GetMapping("")
    public ResponseEntity<Result<List<Product>>> getProductsOfCurrentSeller(Authentication auth) {
        List<Product> products = productService.getProductsOfCurrentSeller(auth);
        Result<List<Product>> response = Result.success(products, "Product's searched by specific sellers id and fetched succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<List<Product>>> getProductsBySellerId(@PathVariable Long id) {
        List<Product> products = productService.getProductsBySellerId(id);
        Result<List<Product>> response = Result.success(products, "Product's searched by connected users id and fetched succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }

    @PostMapping("")
    public ResponseEntity<Result<List<Product>>> createProduct(Authentication connectedUser, @RequestBody CreateProductReqDto request) {
        Seller seller = (Seller) connectedUser.getPrincipal();
        List<Product> product = productService.createProduct(request, seller);
        Result<List<Product>> response = Result.success(product, "Product created succesfully", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Result<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteById(productId);
        Result<Void> response = Result.success("Product deleted succesfully", CREATED.value());
        return ResponseEntity.status(CREATED).body(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Result<Product>> updateProduct(@PathVariable Long productId,
                                                         @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(productId, product);
        Result<Product> response = Result.success(updatedProduct, "Product updated succesfully", OK.value());
        return ResponseEntity.status(CREATED).body(response);
    }

}