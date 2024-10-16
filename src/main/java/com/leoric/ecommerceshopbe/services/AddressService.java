package com.leoric.ecommerceshopbe.services;

import com.leoric.ecommerceshopbe.models.Address;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface AddressService {

    Set<Address> findAllUsersAddresses(Authentication connectedUser);
}
