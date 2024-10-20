package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.Review;
import com.leoric.ecommerceshopbe.requests.CreateReviewRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {

    List<Review> getReviewsByProduct(Long productId);

    Review createReview(CreateReviewRequest req, Authentication authentication, Product product);

    Review updateReview(Long reviewId, String reviewText, double rating, Authentication authentication);

    void deleteById(Long id, Authentication authentication);
    Review findById(Long id);

}