package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Deal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DealService {

    List<Deal> findAll();

    Deal findById(Long id);

    Deal save(Deal entity);

    void deleteById(Long id);
}
