package com.leoric.ecommerceshopbe.security.auth;

import com.leoric.ecommerceshopbe.handler.OtpVerificationException;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.security.JwtProvider;
import com.leoric.ecommerceshopbe.security.auth.dto.AuthenticationResponse;
import com.leoric.ecommerceshopbe.security.auth.dto.SetupPwFromOtpReq;
import com.leoric.ecommerceshopbe.security.auth.dto.SignInRequest;
import com.leoric.ecommerceshopbe.security.auth.dto.SignupRequest;
import com.leoric.ecommerceshopbe.security.auth.email.EmailService;
import com.leoric.ecommerceshopbe.services.interfaces.AuthService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import com.leoric.ecommerceshopbe.services.interfaces.UserService;
import com.leoric.ecommerceshopbe.services.interfaces.VerificationCodeService;
import com.leoric.ecommerceshopbe.utils.OtpUtil;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getAccountFromPrincipal;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeService verificationCodeService;
    private final UserService userService;
    private final EmailService emailService;
    private final SellerService sellerService;
    @Value("${security.otp.length}")
    private int length;

    @Override
    @Transactional
    public void signupAndSendOtp(SignupRequest request) {
        String email = request.getEmail();
        String accountType = request.getAccount();

        if (accountType.equalsIgnoreCase("seller")) {
            Seller seller = sellerService.createSellerFromDto(request);
            sendOtp(email, seller, true);
            return;
        } else if (accountType.equalsIgnoreCase("user")) {
            User user = userService.createUserFromDto(request);
            sendOtp(email, user, false);
            return;
        }
        throw new IllegalArgumentException("Unknown account type. Options so far - user, seller");
    }

    private void sendOtp(String email, Object user, boolean isSeller) {
        boolean isExists = verificationCodeService.existsByEmail(email);
        if (isExists) {
            verificationCodeService.deleteByEmail(email);
        }

        String otp = OtpUtil.generateOtp(length);
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);

        if (isSeller) {
            verificationCode.setSeller((Seller) user);
            verificationCodeService.save(verificationCode);
            String subject = "LEORIC ESHOP OTP verification code";
            emailService.sendVerificationEmail(email, ((Seller) user).getSellerName(), subject, otp);
        } else {
            verificationCode.setUser((User) user);
            verificationCodeService.save(verificationCode);
            String subject = "LEORIC ESHOP OTP verification code";
            emailService.sendVerificationEmail(email, ((User) user).getName(), subject, otp);
        }
    }

    @Override
    @Transactional
    public AccountDetailDto setupPwFromOtp(SetupPwFromOtpReq req) {
        String email = req.getEmail();
        VerificationCode verificationCode;

        if (userService.existsByEmail(email)) {
            verificationCode = verificationCodeService.findByEmail(email);
            User user = userService.findByEmail(email);

            if (!verificationCode.getOtp().equals(req.getOtp())) {
                throw new OtpVerificationException("Invalid OTP");
            }

            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setEnabled(true);
            user.setVerificationCode(null);
            User savedUser = userService.save(user);

            verificationCodeService.deleteByEmail(email);

            return new AccountDetailDto(
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail(),
                    savedUser.getMobile(),
                    savedUser.getRole()
            );
        } else if (sellerService.existsByEmail(email)) {
            verificationCode = verificationCodeService.findByEmail(email);
            Seller seller = sellerService.findByEmail(email);

            if (!verificationCode.getOtp().equals(req.getOtp())) {
                throw new OtpVerificationException("Invalid OTP");
            }

            seller.setPassword(passwordEncoder.encode(req.getPassword()));
            seller.setEmailVerified(true);
            Seller savedSeller = sellerService.save(seller);

            verificationCodeService.deleteByEmail(email);

            return new AccountDetailDto(
                    savedSeller.getId(),
                    savedSeller.getName(),
                    savedSeller.getEmail(),
                    savedSeller.getMobile(),
                    savedSeller.getRole()
            );
        }
        throw new EntityNotFoundException("Not registered email address");
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest req) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        Map<String, Object> claims = new HashMap<>();
        Account account = getAccountFromPrincipal(auth.getPrincipal());
        account.setSignedOut(false);
        if (account instanceof User) {
            userService.save((User) account);
        } else if (account instanceof Seller) {
            sellerService.save((Seller) account);
        }

        claims.put("fullname", account.getName());
        claims.put("email", account.getEmail());

        String jwtToken;
        if (account instanceof User) {
            jwtToken = jwtProvider.generateToken(claims, (User) account);
        } else if (account instanceof Seller) {
            jwtToken = jwtProvider.generateToken(claims, (Seller) account);
        } else {
            throw new IllegalArgumentException("Unknown account type");
        }

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(account.getRole())
                .build();
    }

    @Override
    public void signOut(Authentication authentication) throws Exception {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new Exception("Invalid authentication, principal is null");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof User user) {
            user.setSignedOut(true);
            user.setLastSignOut(LocalDateTime.now());
            User savedUser = userService.save(user);

            if (!savedUser.isSignedOut()) {
                throw new Exception("Something went wrong in user logout");
            }

        } else if (principal instanceof Seller seller) {
            seller.setSignedOut(true);
            seller.setLastSignOut(LocalDateTime.now());
            sellerService.save(seller);

            if (!seller.isSignedOut()) {
                throw new Exception("Something went wrong in seller logout");
            }

        } else {
            throw new Exception("Unknown principal type, cannot sign out");
        }
    }

}