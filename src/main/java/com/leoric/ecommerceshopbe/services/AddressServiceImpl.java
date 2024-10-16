package com.leoric.ecommerceshopbe.services;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AddressServiceImpl implements AddressService {

    @Override
    public Set<Address> findAllUsersAddresses(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        return user.getAddresses();
    }
}
