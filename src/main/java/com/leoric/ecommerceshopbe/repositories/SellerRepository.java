package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
