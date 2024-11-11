package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.handler.EmailAlreadyInUseException;
import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.constants.AccountStatus;
import com.leoric.ecommerceshopbe.models.embeded.BankDetails;
import com.leoric.ecommerceshopbe.models.embeded.BusinessDetails;
import com.leoric.ecommerceshopbe.models.mapstruct.SellerMapper;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.requests.dto.SellerEditRequestDto;
import com.leoric.ecommerceshopbe.security.JwtProvider;
import com.leoric.ecommerceshopbe.security.auth.dto.SignupRequest;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.leoric.ecommerceshopbe.models.constants.USER_ROLE.ROLE_SELLER;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;
    private final SellerMapper sellerMapper;
    private final GlobalUtil globalUtil;

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
        return findByEmail(email);
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
    public Seller findByEmail(String email) {
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
    public Seller updateSeller(Authentication connectedUser, SellerEditRequestDto dto) {
        Seller connectedSeller = globalUtil.getPrincipalAsSeller(connectedUser);
        updateSellerFromDtoWithFieldsCheck(dto, connectedSeller);
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
    @Transactional
    public Seller createSellerFromDto(SignupRequest request) {
        if (sellerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyInUseException("This email is already in use by another seller");
        }
        Seller newSeller = new Seller();
        newSeller.setEmail(request.getEmail());
        newSeller.setSellerName(request.getFullName());
        newSeller.setRole(ROLE_SELLER);
        return sellerRepository.save(newSeller);
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
        return sellerRepository.existsByEmail(email);
    }

    private void updateSellerFromDtoWithFieldsCheck(SellerEditRequestDto dto, Seller connectedSeller) {
        if (dto.getSellerName() != null && !dto.getSellerName().isBlank()) {
            connectedSeller.setSellerName(dto.getSellerName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            connectedSeller.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            connectedSeller.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        if (dto.getVat() != null && !dto.getVat().isBlank()) {
            connectedSeller.setVat(dto.getVat());
        }

        if (dto.getBusinessDetails() != null) {
            BusinessDetails businessDetails = connectedSeller.getBusinessDetails();
            if (dto.getBusinessDetails().getBusinessName() != null && !dto.getBusinessDetails().getBusinessName().isBlank()) {
                businessDetails.setBusinessName(dto.getBusinessDetails().getBusinessName());
            }
            if (dto.getBusinessDetails().getBusinessAddress() != null && !dto.getBusinessDetails().getBusinessAddress().isBlank()) {
                businessDetails.setBusinessAddress(dto.getBusinessDetails().getBusinessAddress());
            }
            if (dto.getBusinessDetails().getBusinessEmail() != null && !dto.getBusinessDetails().getBusinessEmail().isBlank()) {
                businessDetails.setBusinessEmail(dto.getBusinessDetails().getBusinessEmail());
            }
            if (dto.getBusinessDetails().getBusinessMobile() != null && !dto.getBusinessDetails().getBusinessMobile().isBlank()) {
                businessDetails.setBusinessMobile(dto.getBusinessDetails().getBusinessMobile());
            }
            if (dto.getBusinessDetails().getLogo() != null && !dto.getBusinessDetails().getLogo().isBlank()) {
                businessDetails.setLogo(dto.getBusinessDetails().getLogo());
            }
            if (dto.getBusinessDetails().getBanner() != null && !dto.getBusinessDetails().getBanner().isBlank()) {
                businessDetails.setBanner(dto.getBusinessDetails().getBanner());
            }
        }

        if (dto.getBankDetails() != null) {
            BankDetails bankDetails = connectedSeller.getBankDetails();
            if (dto.getBankDetails().getAccountNumber() != null && !dto.getBankDetails().getAccountNumber().isBlank()) {
                bankDetails.setAccountNumber(dto.getBankDetails().getAccountNumber());
            }
            if (dto.getBankDetails().getAccountHolderName() != null && !dto.getBankDetails().getAccountHolderName().isBlank()) {
                bankDetails.setAccountHolderName(dto.getBankDetails().getAccountHolderName());
            }
            if (dto.getBankDetails().getIban() != null && !dto.getBankDetails().getIban().isBlank()) {
                bankDetails.setIban(dto.getBankDetails().getIban());
            }
        }

        if (dto.getPickupAddress() != null) {
            Address pickupAddress = connectedSeller.getPickupAddress();
            if (dto.getPickupAddress().getName() != null && !dto.getPickupAddress().getName().isBlank()) {
                pickupAddress.setName(dto.getPickupAddress().getName());
            }
            if (dto.getPickupAddress().getStreet() != null && !dto.getPickupAddress().getStreet().isBlank()) {
                pickupAddress.setStreet(dto.getPickupAddress().getStreet());
            }
            if (dto.getPickupAddress().getLocality() != null && !dto.getPickupAddress().getLocality().isBlank()) {
                pickupAddress.setLocality(dto.getPickupAddress().getLocality());
            }
            if (dto.getPickupAddress().getCity() != null && !dto.getPickupAddress().getCity().isBlank()) {
                pickupAddress.setCity(dto.getPickupAddress().getCity());
            }
            if (dto.getPickupAddress().getCountry() != null && !dto.getPickupAddress().getCountry().isBlank()) {
                pickupAddress.setCountry(dto.getPickupAddress().getCountry());
            }
            if (dto.getPickupAddress().getZip() != null && !dto.getPickupAddress().getZip().isBlank()) {
                pickupAddress.setZip(dto.getPickupAddress().getZip());
            }
            if (dto.getPickupAddress().getMobile() != null && !dto.getPickupAddress().getMobile().isBlank()) {
                pickupAddress.setMobile(dto.getPickupAddress().getMobile());
            }
        }
    }

}