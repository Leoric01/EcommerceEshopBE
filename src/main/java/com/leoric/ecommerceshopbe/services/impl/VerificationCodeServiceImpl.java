package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.VerificationCode;
import com.leoric.ecommerceshopbe.repositories.VerificationCodeRepository;
import com.leoric.ecommerceshopbe.services.interfaces.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private final VerificationCodeRepository verificationcodeRepository;

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
}
