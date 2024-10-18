package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.SellerReport;
import com.leoric.ecommerceshopbe.repositories.SellerReportRepository;
import com.leoric.ecommerceshopbe.services.interfaces.SellerReportService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(Seller seller) {
        Optional<SellerReport> sellerReport = sellerReportRepository.findBySellerId(seller.getId());
        if (sellerReport.isEmpty()) {
            SellerReport newSellerReport = new SellerReport();
            newSellerReport.setSeller(seller);
            return sellerReportRepository.save(newSellerReport);
        }
        return sellerReport.get();
    }

    @Override
    @Transactional
    public SellerReport updateSellerReport(SellerReport sellerReport) {
        return sellerReportRepository.save(sellerReport);
    }

    @Override
    public List<SellerReport> findAll() {
        return sellerReportRepository.findAll();
    }

    @Override
    public SellerReport findById(Long id) {
        return sellerReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SellerReport not found"));
    }

    @Override
    public SellerReport save(SellerReport entity) {
        return sellerReportRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        sellerReportRepository.deleteById(id);
    }
}
