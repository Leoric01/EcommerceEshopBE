package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
