package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
