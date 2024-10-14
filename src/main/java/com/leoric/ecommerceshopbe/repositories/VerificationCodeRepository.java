package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
}
