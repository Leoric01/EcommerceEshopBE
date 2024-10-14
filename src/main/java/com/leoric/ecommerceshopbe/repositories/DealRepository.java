package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {
}
