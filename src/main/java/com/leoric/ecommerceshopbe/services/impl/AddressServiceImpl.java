package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AddressServiceImpl implements AddressService {

    @Override
    public Set<Address> findAllUsersAddresses(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        return user.getAddresses();
    }

    @Override
    public List<Address> findAll() {
        return List.of();
    }

    @Override
    public Address findById(Long id) {
        return null;
    }

    @Override
    public Address save(Address entity) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
