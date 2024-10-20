package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.security.auth.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<AccountDetailDto> findAllUsersToDto();

    User findById(Long id);

    User save(User entity);

    void deleteById(Long id);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findUserByJwtToken(String jwtToken);

    AccountDetailDto currentUser(Authentication connectedUser);
}