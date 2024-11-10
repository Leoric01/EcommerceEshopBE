package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.handler.InvalidAccountTypeAccessException;
import com.leoric.ecommerceshopbe.models.Address;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.mapstruct.AddressMapper;
import com.leoric.ecommerceshopbe.repositories.AddressRepository;
import com.leoric.ecommerceshopbe.repositories.SellerRepository;
import com.leoric.ecommerceshopbe.requests.dto.AddAddressRequestDTO;
import com.leoric.ecommerceshopbe.response.AddressDtoResponse;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.UserRepository;
import com.leoric.ecommerceshopbe.services.interfaces.AddressService;
import com.leoric.ecommerceshopbe.services.interfaces.UserService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final GlobalUtil globalUtil;
    private final UserService userService;
    private final AddressMapper addressMapper;

    @Override
    public void deleteAddress(Authentication authentication, Long addressId) {
        Account account = globalUtil.getAccountFromAuthentication(authentication);
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found"));
        if (account instanceof User user) {
            if (address.getUser() == null || !address.getUser().getId().equals(user.getId())) {
                throw new AccessDeniedException("This address does not belong to the current user.");
            }
            User fetchedUser = userService.findById(user.getId());
            fetchedUser.getAddresses().remove(address);
            userRepository.save(fetchedUser);
            deleteById(addressId);
        } else if (account instanceof Seller seller) {
            if (address.getSeller() == null || !address.getSeller().getId().equals(seller.getId())) {
                throw new AccessDeniedException("This address does not belong to the current seller.");
            }
            Seller fetchedSeller = sellerRepository.findById(seller.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Seller not found"));
            fetchedSeller.setPickupAddress(null);
            sellerRepository.save(fetchedSeller);
            deleteById(addressId);
        } else {
            throw new InvalidAccountTypeAccessException("Access denied: Unrecognized account type.");
        }
    }

    private static Address getAddress(AddAddressRequestDTO addressDto) {
        Address address = new Address();
        setAddressFieldsFromDto(addressDto, address);
        return address;
    }

    private static void setAddressFieldsFromDto(AddAddressRequestDTO address, Address addressEdit) {
        if (address.getName() != null && !address.getName().isBlank()) {
            addressEdit.setName(address.getName());
        }
        if (address.getStreet() != null && !address.getStreet().isBlank()) {
            addressEdit.setStreet(address.getStreet());
        }
        if (address.getLocality() != null && !address.getLocality().isBlank()) {
            addressEdit.setLocality(address.getLocality());
        }
        if (address.getZip() != null && !address.getZip().isBlank()) {
            addressEdit.setZip(address.getZip());
        }
        if (address.getCity() != null && !address.getCity().isBlank()) {
            addressEdit.setCity(address.getCity());
        }
        if (address.getCountry() != null && !address.getCountry().isBlank()) {
            addressEdit.setCountry(address.getCountry());
        }
        if (address.getMobile() != null && !address.getMobile().isBlank()) {
            addressEdit.setMobile(address.getMobile());
        }
    }

    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    @Override
    public Address editUserAddress(Long userId, AddAddressRequestDTO addressDto, Long addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Address addressToEdit = user.getAddresses().stream()
                .filter(address -> address.getId().equals(addressId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Address not found with ID: " + addressId + " for user ID: " + userId));

        setAddressFieldsFromDto(addressDto, addressToEdit);
        return addressRepository.save(addressToEdit);
    }

    @Override
    public Address editSellerAddress(Authentication authentication, AddAddressRequestDTO address) {
        Seller seller = globalUtil.getPrincipalAsSeller(authentication);
        Address addressEdit = seller.getPickupAddress();
        setAddressFieldsFromDto(address, addressEdit);
        seller.setPickupAddress(addressEdit);
        addressRepository.save(addressEdit);
        sellerRepository.save(seller);
        return addressEdit;
    }

    @Override
    public Address addUserAddress(Long userId, AddAddressRequestDTO addressDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Address address = addressMapper.toAddress(addressDto);
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public Address addSellerAddress(Long sellerId, AddAddressRequestDTO addressDto) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("Seller not found"));
        Address existingAddress = seller.getPickupAddress();
        if (existingAddress != null) {
            addressMapper.updateAddressFromDto(addressDto, existingAddress);
            return addressRepository.save(existingAddress);
        } else {
            Address newAddress = addressMapper.toAddress(addressDto);
            newAddress.setSeller(seller);
            Address savedAddress = addressRepository.save(newAddress);
            seller.setPickupAddress(savedAddress);
            sellerRepository.save(seller);
            return savedAddress;
        }
    }

    @Override
    @Transactional
    public Set<AddressDtoResponse> findSellerAddress(Authentication connectedAccount) {
        Seller seller = globalUtil.getPrincipalAsSeller(connectedAccount);
        List<Address> addresses = Collections.singletonList(seller.getPickupAddress());
        return addresses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public Set<AddressDtoResponse> findAllUsersAddresses(Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        if (user.getRole().contains("ADMIN")) {
            List<Address> allAddresses = addressRepository.findAll();
            return allAddresses.stream().map(this::convertToDto).collect(Collectors.toSet());
        }
        User fetchedFromDb = userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Set<Address> addresses = fetchedFromDb.getAddresses();

        return addresses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public List<Address> findAll() {
        return addressRepository.findAll();
    }

    @Override
    public Address findById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Address not found"));
    }

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
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
        dto.setOrders(address.getOrders() != null ? address.getOrders() : null);
        dto.setSeller_id(address.getSeller() != null ? address.getSeller().getId() : null);

        return dto;
    }
}