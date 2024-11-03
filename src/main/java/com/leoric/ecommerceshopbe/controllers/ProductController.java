package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.response.common.Result;
import com.leoric.ecommerceshopbe.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        Hibernate.initialize(product.getSeller());  // Initialize lazy-loaded fields
        Hibernate.initialize(product.getReviews());
        Result<Product> response = Result.success(product, "Product's details fetched successfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Result<List<Product>>> searchProduct(@RequestParam(required = false) String query) {
        List<Product> product = productService.searchProducts(query);
        Result<List<Product>> response = Result.success(product, "Product's searched and fetched succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);

    }

    @GetMapping("/")
    public ResponseEntity<Result<Page<Product>>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String colors,
            @RequestParam(required = false) String sizes,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) Integer minDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            @RequestParam(defaultValue = "0") Integer pageNumber) {
        Page<Product> products = productService.getAllProducts(category, brand, colors, sizes, minPrice, maxPrice, minDiscount, sort, stock, pageNumber);
        for (Product product : products.getContent()) {
            Hibernate.initialize(product.getSeller());
            Hibernate.initialize(product.getReviews());
        }
        Result<Page<Product>> response = Result.success(products, "Products fetched succesfully", OK.value());
        return ResponseEntity.status(OK).body(response);
    }
}