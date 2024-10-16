package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.SellerReport;
import com.leoric.ecommerceshopbe.repositories.SellerReportRepository;
import com.leoric.ecommerceshopbe.services.interfaces.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerreportRepository;

    @Override
    public List<SellerReport> findAll() {
        return sellerreportRepository.findAll();
    }

    @Override
    public SellerReport findById(Long id) {
        return sellerreportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SellerReport not found"));
    }

    @Override
    public SellerReport save(SellerReport entity) {
        return sellerreportRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        sellerreportRepository.deleteById(id);
    }
}
