package com.leoric.ecommerceshopbe.services.impl;

import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.repositories.UserRepository;
import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.security.JwtProvider;
import com.leoric.ecommerceshopbe.services.interfaces.UserService;
import com.leoric.ecommerceshopbe.utils.GlobalUtil;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public List<AccountDetailDto> findAllUsersToDto() {
        List<User> usersList = userRepository.findAll();
        return usersList.stream()
                .map(this::getAccountDto)
                .toList();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with " + email + " email was not found"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findUserByJwtToken(String jwtToken) {
        String email = jwtProvider.extractEmailFromJwt(jwtToken);
        return findByEmail(email);
    }

    @Override
    public AccountDetailDto currentUser(Authentication connectedUser) {
        Account account = GlobalUtil.getAccountFromPrincipal(connectedUser.getPrincipal());
        return getAccountDto(account);
    }

    private AccountDetailDto getAccountDto(Account account) {
        AccountDetailDto accountDetailDto = new AccountDetailDto();
        accountDetailDto.setId(account.getId());
        accountDetailDto.setName(account.getName());
        accountDetailDto.setEmail(account.getEmail());
        accountDetailDto.setMobile(account.getMobile());
        accountDetailDto.setRole(account.getRole());
        return accountDetailDto;
    }
}
