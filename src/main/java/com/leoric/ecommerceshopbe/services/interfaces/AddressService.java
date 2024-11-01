package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface AddressService {
    Set<Address> findAllUsersAddresses(Authentication connectedUser);

    List<Address> findAll();

    Address findById(Long id);

    Address save(Address entity);

    void deleteById(Long id);

    Address addUserAddress(Long userId, AddAddressRequestDTO address);

    Address addSellerAddress(Long sellerId, AddAddressRequestDTO address);
}