package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Deal;
import com.leoric.ecommerceshopbe.models.HomeCategory;
import com.leoric.ecommerceshopbe.repositories.DealRepository;
import com.leoric.ecommerceshopbe.repositories.HomeCategoryRepository;
import com.leoric.ecommerceshopbe.services.interfaces.DealService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealServiceImpl implements DealService {

    private final DealRepository dealRepository;
    private final HomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        Deal newDeal = new Deal();
        newDeal.setCategory(category);
        newDeal.setDiscount(deal.getDiscount());
        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDealById(Long id, Deal deal) {
        Deal oldDeal = dealRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Deal not found"));
        HomeCategory category = homeCategoryRepository.findById(deal.getCategory().getId()).orElse(null);
        if (deal.getDiscount() != null) {
            oldDeal.setDiscount(deal.getDiscount());
        }
        if (category != null) {
            oldDeal.setCategory(category);
        }
        return dealRepository.save(oldDeal);
    }

    @Override
    public void deleteDealById(Long id) {
        if (!dealRepository.existsById(id)) {
            throw new EntityNotFoundException("Deal not found");
        }
        dealRepository.deleteById(id);
    }

    @Override
    public Deal getDealById(Long id) {
        return dealRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Deal not found"));
    }
}