package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.repositories.AddressRepository;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.response.AddressDtoResponse;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    private static Address getAddress(AddAddressRequestDTO addressDto) {
        Address address = new Address();
        address.setName(addressDto.getName());
        address.setStreet(addressDto.getStreet());
        address.setLocality(addressDto.getLocality());
        address.setZip(addressDto.getZip());
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setMobile(addressDto.getMobile());
        return address;
    }

    public Address addUserAddress(Long userId, AddAddressRequestDTO addressDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Address address = getAddress(addressDto);
        address.setUser(user);
        return addressRepository.save(address);
    }

    // Done like that so seller will have only one address where business is, and in business details
    // address is where the business is registered with the state
    public Address addSellerAddress(Long sellerId, AddAddressRequestDTO addressDto) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));

        Address existingAddress = seller.getPickupAddress();
        if (existingAddress != null) {
            existingAddress.setStreet(addressDto.getStreet());
            existingAddress.setCity(addressDto.getCity());
            existingAddress.setCountry(addressDto.getCountry());
            existingAddress.setZip(addressDto.getZip());
            existingAddress.setLocality(addressDto.getLocality());
            existingAddress.setMobile(addressDto.getMobile());
            existingAddress.setName(addressDto.getName());
            return addressRepository.save(existingAddress);
        } else {
            Address newAddress = new Address();
            newAddress.setName(addressDto.getName());
            newAddress.setStreet(addressDto.getStreet());
            newAddress.setCity(addressDto.getCity());
            newAddress.setCountry(addressDto.getCountry());
            newAddress.setZip(addressDto.getZip());
            newAddress.setLocality(addressDto.getLocality());
            newAddress.setMobile(addressDto.getMobile());
            newAddress.setSeller(seller);
            Address savedAddress = addressRepository.save(newAddress);
            seller.setPickupAddress(savedAddress);
            sellerRepository.save(seller);
            return savedAddress;
        }
    }
    @Override
    @Transactional
    public Set<AddressDtoResponse> findAllUsersAddresses(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        User fetchedFromDb = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Set<Address> addresses = fetchedFromDb.getAddresses();

        return addresses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    private AddressDtoResponse convertToDto(Address address) {
        AddressDtoResponse dto = new AddressDtoResponse();
        dto.setId(address.getId());

        dto.setName(address.getName() != null ? address.getName() : "");
        dto.setStreet(address.getStreet() != null ? address.getStreet() : "");
        dto.setLocality(address.getLocality() != null ? address.getLocality() : "");
        dto.setZip(address.getZip() != null ? address.getZip() : "");
        dto.setCity(address.getCity() != null ? address.getCity() : "");
        dto.setCountry(address.getCountry() != null ? address.getCountry() : "");
        dto.setMobile(address.getMobile() != null ? address.getMobile() : "");

        dto.setUser_id(address.getUser() != null ? address.getUser().getId() : null);
        dto.setOrder_id(address.getOrder() != null ? address.getOrder().getId() : null);
        dto.setSeller_id(address.getSeller() != null ? address.getSeller().getId() : null);

        return dto;
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
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public void deleteById(Long id) {
    }
}