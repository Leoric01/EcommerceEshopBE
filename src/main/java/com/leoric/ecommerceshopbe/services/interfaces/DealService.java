package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Deal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DealService {

    Deal updateDealById(Long id, Deal deal);
    void deleteDealById(Long id);

    Deal createDeal(Deal deal);

    List<Deal> getAllDeals();

    Deal getDealById(Long id);
}