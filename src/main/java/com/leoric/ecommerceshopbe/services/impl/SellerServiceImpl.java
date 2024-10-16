package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.handler.EmailAlreadyInUseException;
import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.constants.AccountStatus;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.security.JwtProvider;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

import static com.leoric.ecommerceshopbe.models.constants.USER_ROLE.ROLE_SELLER;
import static com.leoric.ecommerceshopbe.utils.GlobalUtil.isNotBlank;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;

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
    public Seller createSeller(Seller seller) {
        if (sellerRepository.findByEmail(seller.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("This email is already in use by another seller");
        }
        Address savedAddress = addressService.save(seller.getPickupAddress());
        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with  id number " + id + " not found"));
    }

    @Override
    public Seller getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller newDataSeller) {
        Seller patchedSeller = getSellerById(id);
        // Update Seller fields if not blank
        updateIfPresent(newDataSeller.getEmail(), patchedSeller::setEmail);
        updateIfPresent(newDataSeller.getSellerName(), patchedSeller::setSellerName);
        updateIfPresent(newDataSeller.getGSTIN(), patchedSeller::setGSTIN);
        updateIfPresent(newDataSeller.getMobile(), patchedSeller::setMobile);
        // Update BankDetails if present
        if (newDataSeller.getBankDetails() != null) {
            updateIfPresent(newDataSeller.getBankDetails().getAccountHolderName(),
                    patchedSeller.getBankDetails()::setAccountHolderName);
            updateIfPresent(newDataSeller.getBankDetails().getIfscCode(),
                    patchedSeller.getBankDetails()::setIfscCode);
            updateIfPresent(newDataSeller.getBankDetails().getAccountNumber(),
                    patchedSeller.getBankDetails()::setAccountNumber);
        }
        // Update BusinessDetails if present
        if (newDataSeller.getBusinessDetails() != null) {
            updateIfPresent(newDataSeller.getBusinessDetails().getBusinessName(),
                    patchedSeller.getBusinessDetails()::setBusinessName);
        }
        // Update PickupAddress if present
        if (newDataSeller.getPickupAddress() != null) {
            updateIfPresent(newDataSeller.getPickupAddress().getAddress(),
                    patchedSeller.getPickupAddress()::setAddress);
            updateIfPresent(newDataSeller.getPickupAddress().getState(),
                    patchedSeller.getPickupAddress()::setState);
            updateIfPresent(newDataSeller.getPickupAddress().getCity(),
                    patchedSeller.getPickupAddress()::setCity);
            updateIfPresent(newDataSeller.getPickupAddress().getMobile(),
                    patchedSeller.getPickupAddress()::setMobile);
            updateIfPresent(newDataSeller.getPickupAddress().getPinCode(),
                    patchedSeller.getPickupAddress()::setPinCode);
            // Save updated PickupAddress
            addressService.save(patchedSeller.getPickupAddress());
        }
        return sellerRepository.save(patchedSeller);
    }

    @Override
    public void deleteSeller(Long id) {

    }

    @Override
    public Seller verifyEmail(String email, String otp) {
        return null;
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) {
        return null;
    }

    private void updateIfPresent(String value, Consumer<String> setter) {
        if (isNotBlank(value)) {
            setter.accept(value);
        }
    }
}
