package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<Result<Product>> getProduct(@PathVariable Long productId) {
        Product product = productService.findProductById(productId);
        Result<Product> response = Result.success(product, "Product's details fetched succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }
}
