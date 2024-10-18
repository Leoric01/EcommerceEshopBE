package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.handler.EmailAlreadyInUseException;
import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.constants.AccountStatus;
import com.leoric.ecommerceshopbe.models.mapstruct.SellerMapper;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.repositories.UserRepository;
import com.leoric.ecommerceshopbe.security.JwtProvider;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getAccountFromPrincipal;
import static com.leoric.ecommerceshopbe.utils.GlobalUtil.isNotBlank;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final UserRepository userRepository;
    private final SellerMapper sellerMapper;

    @Override
    public List<Seller> findAll() {
        return sellerRepository.findAll();
    }

    @Override
    public Seller findById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seller not found"));
    }

    @Override
    public Seller save(Seller entity) {
        return sellerRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        sellerRepository.deleteById(id);
    }

    @Override
    public Seller getSellerProfile(String jwt) {
        String email = jwtProvider.extractEmailFromJwt(jwt);
        return getSellerByEmail(email);
    }

    @Override
    @Transactional
    public Seller createSeller(Seller seller) {
        if (sellerRepository.findByEmail(seller.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("This email is already in use by another seller");
        }
        Seller newSeller = sellerMapper.mapWithoutPassword(seller);
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        if (seller.getPickupAddress() != null) {
            Address savedAddress = addressService.save(seller.getPickupAddress());
            newSeller.setPickupAddress(savedAddress);
        } else {
            throw new IllegalArgumentException("Address cannot be null");
        }
        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seller with  id number " + id + " not found"));
    }

    @Override
    public Seller getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        if (status != null && !status.name().isBlank()) {
            return sellerRepository.findByAccountStatus(status);
        } else {
            return sellerRepository.findAll();
        }
    }

    @Override
    @Transactional
    public Seller updateSeller(Authentication connectedUser, Seller newDataSeller) {
        Account account = getAccountFromPrincipal(connectedUser.getPrincipal());
        Seller connectedSeller = (Seller) account;
        sellerMapper.updateSellerFromSeller(newDataSeller, connectedSeller);
        if (connectedSeller.getPickupAddress() != null) {
            addressService.save(connectedSeller.getPickupAddress());
        }
        return sellerRepository.save(connectedSeller);
    }

    @Override
    public Seller updateSellerById(Long id, Seller seller) {
        Seller updatedSeller = getSellerById(id);
        //                                  source, target
        sellerMapper.updateSellerFromSeller(seller, updatedSeller);
        if (updatedSeller.getPickupAddress() != null) {
            addressService.save(updatedSeller.getPickupAddress());
        }
        return sellerRepository.save(updatedSeller);
    }

    @Override
    public void deleteSeller(Long id) {
        sellerRepository.deleteById(id);
    }


    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }


    //prob redundant cause mapstruct
    private void updateIfPresent(String value, Consumer<String> setter) {
        if (isNotBlank(value)) {
            setter.accept(value);
        }
    }
}
