package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerReportRepository extends JpaRepository<SellerReport, Long> {
    Optional<SellerReport> findBySellerId(Long sellerId);
}
