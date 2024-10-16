package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Seller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SellerService {

    List<Seller> findAll();

    Seller findById(Long id);

    Seller save(Seller entity);

    void deleteById(Long id);
}
