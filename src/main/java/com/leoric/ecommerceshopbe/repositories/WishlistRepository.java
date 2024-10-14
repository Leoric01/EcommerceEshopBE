package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
}
