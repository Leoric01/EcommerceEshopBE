package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.SellerReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SellerReportService {

    List<SellerReport> findAll();

    SellerReport findById(Long id);

    SellerReport save(SellerReport entity);

    void deleteById(Long id);
}
