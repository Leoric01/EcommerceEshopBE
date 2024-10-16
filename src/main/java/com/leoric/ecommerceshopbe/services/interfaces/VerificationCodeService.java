package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.VerificationCode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VerificationCodeService {

    List<VerificationCode> findAll();

    VerificationCode findById(Long id);

    VerificationCode save(VerificationCode entity);

    void deleteById(Long id);
}
