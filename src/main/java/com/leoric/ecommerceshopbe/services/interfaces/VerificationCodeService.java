package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.security.auth.VerificationCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VerificationCodeService {
    void deleteByEmail(String email);

    VerificationCode findByEmail(String email);

    List<VerificationCode> findAll();

    VerificationCode findById(Long id);

    VerificationCode save(VerificationCode entity);

    void deleteById(Long id);

    boolean existsByEmail(String email);
}
