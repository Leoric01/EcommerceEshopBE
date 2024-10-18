package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.constants.AccountStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SellerService {

    List<Seller> findAll();

    Seller findById(Long id);

    Seller save(Seller entity);

    void deleteById(Long id);

    Seller getSellerProfile(String jwt);

    Seller createSeller(Seller seller);

    Seller getSellerById(Long id);

    Seller getSellerByEmail(String email);

    List<Seller> getAllSellers(AccountStatus status);

    Seller updateSeller(Authentication connectedUser, Seller seller);

    void deleteSeller(Long id);

    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status);

    boolean existsByEmail(String email);

    Seller updateSellerById(Long id, Seller seller);
}
