package com.leoric.ecommerceshopbe.services.interfaces;

import com.leoric.ecommerceshopbe.requests.dto.UserUpdateReqDto;
import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.security.auth.User;
import com.leoric.ecommerceshopbe.security.auth.dto.SignupRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User getUserProfile(Authentication connectedUser);
    List<AccountDetailDto> findAllUsersToDto();

    User findById(Long id);

    User save(User entity);

    void deleteById(Long id);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findUserByJwtToken(String jwtToken);

    AccountDetailDto currentUser(Authentication connectedUser);

    User updateUser(Authentication connectedUser, UserUpdateReqDto userReq);

    User createUserFromDto(SignupRequest request);
}