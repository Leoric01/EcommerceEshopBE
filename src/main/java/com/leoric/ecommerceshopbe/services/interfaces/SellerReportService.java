package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.SellerReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);

    SellerReport updateSellerReport(SellerReport sellerReport);

    List<SellerReport> findAll();

    SellerReport findById(Long id);

    SellerReport save(SellerReport entity);

    void deleteById(Long id);
}
