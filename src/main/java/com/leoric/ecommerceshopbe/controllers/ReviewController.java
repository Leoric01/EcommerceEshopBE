package com.leoric.ecommerceshopbe.controllers;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.Review;
import com.leoric.ecommerceshopbe.requests.CreateReviewRequest;
import com.leoric.ecommerceshopbe.response.ApiResponse;
import com.leoric.ecommerceshopbe.services.interfaces.ProductService;
import com.leoric.ecommerceshopbe.services.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok().body(reviewService.getReviewsByProduct(productId));
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> writeReview(@RequestBody CreateReviewRequest reviewRequest,
                                              @PathVariable Long productId,
                                              Authentication authentication) {
        Product product = productService.findProductById(productId);
        Review review = reviewService.createReview(reviewRequest, authentication, product);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(@RequestBody CreateReviewRequest reviewRequest,
                                               @PathVariable Long reviewId,
                                               Authentication authentication) {
        Review review = reviewService.updateReview(reviewId, reviewRequest.getReviewText(), reviewRequest.getRating(), authentication);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId,
                                                    Authentication authentication) {
        reviewService.deleteById(reviewId, authentication);
        ApiResponse res = new ApiResponse();
        res.setMessage("Review deleted successfully");
        return ResponseEntity.ok().build();
    }
}