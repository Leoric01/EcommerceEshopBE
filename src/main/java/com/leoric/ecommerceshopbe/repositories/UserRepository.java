package com.leoric.ecommerceshopbe.repositories;

import com.leoric.ecommerceshopbe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String userEmail);

    boolean existsByEmail(String email);
}
