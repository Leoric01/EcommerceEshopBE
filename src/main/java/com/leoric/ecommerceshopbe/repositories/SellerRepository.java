package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.constants.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByEmail(String email);

    List<Seller> findByAccountStatus(AccountStatus accountStatus);
    boolean existsByEmail(String email);
}