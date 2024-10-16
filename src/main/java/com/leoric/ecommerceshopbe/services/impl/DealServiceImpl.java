package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Deal;
import com.leoric.ecommerceshopbe.repositories.DealRepository;
import com.leoric.ecommerceshopbe.services.interfaces.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;

    @Override
    public List<Deal> findAll() {
        return dealRepository.findAll();
    }

    @Override
    public Deal findById(Long id) {
        return dealRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Deal not found"));
    }

    @Override
    public Deal save(Deal entity) {
        return dealRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        dealRepository.deleteById(id);
    }
}
