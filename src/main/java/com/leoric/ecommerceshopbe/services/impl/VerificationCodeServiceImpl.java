package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.security.auth.VerificationCode;
import com.leoric.ecommerceshopbe.security.auth.VerificationCodeRepository;
import com.leoric.ecommerceshopbe.services.interfaces.VerificationCodeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationcodeRepository;


    @Override
    public void deleteByEmail(String email) {
        verificationcodeRepository.deleteByEmail(email);
    }

    @Override
    public VerificationCode findByEmail(String email) {
        return verificationcodeRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Invalid or non-existent verification code related to " + email + " email"));
    }

    @Override
    public List<VerificationCode> findAll() {
        return verificationcodeRepository.findAll();
    }

    @Override
    public VerificationCode findById(Long id) {
        return verificationcodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("VerificationCode not found"));
    }

    @Override
    public VerificationCode save(VerificationCode entity) {
        return verificationcodeRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        verificationcodeRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return verificationcodeRepository.existsByEmail(email);
    }
}
