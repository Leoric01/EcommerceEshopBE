package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Product;
import com.leoric.ecommerceshopbe.models.Review;
import com.leoric.ecommerceshopbe.repositories.ProductRepository;
import com.leoric.ecommerceshopbe.repositories.ReviewRepository;
import com.leoric.ecommerceshopbe.requests.CreateReviewRequest;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.services.interfaces.ReviewService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final GlobalUtil globalUtil;


    @Override
    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepository.findAllByProductId(productId);
    }

    @Override
    public Review createReview(CreateReviewRequest req, Authentication authentication, Product product) {
        User user = globalUtil.getPrincipalAsUser(authentication);
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(req.getRating());
        review.setProductImages(req.getImages());
        product.getReviews().add(review);
        productRepository.save(product);
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Authentication authentication) {
        Review review = findById(reviewId);
        User user = globalUtil.getPrincipalAsUser(authentication);
        if (!review.getUser().getId().equals(user.getId())) {
            throw new BadCredentialsException("You do not have permission to edit this review");
        }
        review.setReviewText(reviewText);
        review.setRating(rating);
        return reviewRepository.save(review);
    }

    @Override
    public void deleteById(Long id, Authentication authentication) {
        Review review = findById(id);
        User user = globalUtil.getPrincipalAsUser(authentication);
        if (!review.getUser().getId().equals(user.getId())) {
            throw new BadCredentialsException("You do not have permission to delete this review");
        }
        user.getReviews().remove(review);
        review.setUser(null);
        userRepository.save(user);
        reviewRepository.delete(review);

    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }
}