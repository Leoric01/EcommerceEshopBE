package com.leoric.ecommerceshopbe.services.nointerface;

import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.models.VerificationCode;
import com.leoric.ecommerceshopbe.repositories.CartRepository;
import com.leoric.ecommerceshopbe.requests.SetupPwFromOtpReq;
import com.leoric.ecommerceshopbe.requests.SignInRequest;
import com.leoric.ecommerceshopbe.requests.SignupRequest;
import com.leoric.ecommerceshopbe.requests.VerificationCodeReq;
import com.leoric.ecommerceshopbe.response.AuthenticationResponse;
import com.leoric.ecommerceshopbe.response.UserDto;
import com.leoric.ecommerceshopbe.security.JwtProvider;
import com.leoric.ecommerceshopbe.services.email.EmailService;
import com.leoric.ecommerceshopbe.services.interfaces.AuthService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import com.leoric.ecommerceshopbe.services.interfaces.UserService;
import com.leoric.ecommerceshopbe.services.interfaces.VerificationCodeService;
import com.leoric.ecommerceshopbe.utils.OtpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.leoric.ecommerceshopbe.models.constants.USER_ROLE.ROLE_SELLER;
import static com.leoric.ecommerceshopbe.models.constants.USER_ROLE.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String SELLER_PREFIX = "seller_";
    private static final String USER_PREFIX = "seller_";
    private static final String SIGNING_PREFIX = "signing_";

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final VerificationCodeService verificationCodeService;
    private final UserService userService;
    private final EmailService emailService;
    private final SellerService sellerService;
    @Value("${security.otp.length}")
    private int length;

    @Override
    public void signup(SignupRequest request) {
//        VerificationCode verificationCode = verificationCodeService.findByEmail(request.getEmail());
//
//        if (!verificationCode.getOtp().equals(request.getOtp())) {
//            throw new OtpVerificationException("Invalid or expired OTP");
//        }
//
//        if (userService.existsByEmail(request.getEmail())) {
//            throw new EmailAlreadyInUseException("Email is already in use");
//        }
        if (request.getEmail().startsWith(SELLER_PREFIX)) {
            Seller seller = new Seller();
            seller.setEmail(request.getEmail().substring(SELLER_PREFIX.length()));
            seller.setSellerName(request.getFullName());
            seller.setRole(ROLE_SELLER);
            sellerService.save(seller);
        } else if (request.getEmail().startsWith(USER_PREFIX)) {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail().substring(USER_PREFIX.length()))
                    .role(ROLE_USER)
                    .build();
            user = userService.save(user);
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }
        throw new BadCredentialsException("Invalid email address or role prefix");
    }

    @Override
    public void sentLoginOtp(@Valid VerificationCodeReq req) {
        String email = req.getEmail();
        if (email.startsWith(SIGNING_PREFIX + SELLER_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length() + SELLER_PREFIX.length());
            Seller seller = sellerService.getSellerByEmail(email);
            boolean isExists = verificationCodeService.existsByEmail(email);
            if (isExists) {
                verificationCodeService.deleteByEmail(email);
            }
            String otp = OtpUtil.generateOtp(length);

            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setEmail(email);
            verificationCode.setOtp(otp);
            verificationCode.setSeller(seller);
            verificationCodeService.save(verificationCode);
//            user.setVerificationCode(verificationCode);
//            userService.save(user);
            String subject = "LEORIC ESHOP OTP verification code";
            emailService.sendVerificationEmail(email, seller.getName(), subject, otp);
        } else if (email.startsWith(SIGNING_PREFIX + USER_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length() + USER_PREFIX.length());
            User user = userService.findByEmail(email);
            boolean isExists = verificationCodeService.existsByEmail(email);
            if (isExists) {
                verificationCodeService.deleteByEmail(email);
            }
            String otp = OtpUtil.generateOtp(length);

            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setEmail(email);
            verificationCode.setOtp(otp);
            verificationCode.setUser(user);
            verificationCodeService.save(verificationCode);
//            user.setVerificationCode(verificationCode);
//            userService.save(user);
            String subject = "LEORIC ESHOP OTP verification code";
            emailService.sendVerificationEmail(email, user.getName(), subject, otp);
        }
    }


    @Override
    public UserDto setupPwFromOtp(SetupPwFromOtpReq req) throws BadRequestException {
        String email;
        VerificationCode verificationCode;
        if (req.getEmail().startsWith(USER_PREFIX)) {
            email = req.getEmail().substring(USER_PREFIX.length());
            verificationCode = verificationCodeService.findByEmail(email);
            User user = verificationCode.getUser();

            if (!user.getVerificationCode().getOtp().equals(req.getOtp())) {
                throw new BadRequestException("Invalid OTP");
            }
            user.setPassword(passwordEncoder.encode(req.getPassword()));
            user.setEnabled(true);
            User savedUser = userService.save(user);
            return new UserDto(
                    savedUser.getId(),
                    savedUser.getName(),
                    savedUser.getEmail(),
                    savedUser.getMobile(),
                    savedUser.getRole().name()
            );
        } else if (req.getEmail().startsWith(SELLER_PREFIX)) {
            email = req.getEmail().substring(SELLER_PREFIX.length());
            verificationCode = verificationCodeService.findByEmail(email);
            Seller seller = sellerService.getSellerByEmail(email);
            if (!verificationCode.getOtp().equals(req.getOtp())) {
                throw new BadRequestException("Invalid OTP");
            }
            seller.setPassword(passwordEncoder.encode(req.getPassword()));
            Seller savedSeller = sellerService.save(seller);
            return new UserDto(
                    savedSeller.getId(),
                    savedSeller.getName(),
                    savedSeller.getEmail(),
                    savedSeller.getMobile(),
                    savedSeller.getRole().name()
            );
        }
        throw new BadRequestException("Invalid email address or role prefix");
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest req) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        Map<String, Object> claims = new HashMap<>();
        if (auth.getAuthorities().contains(ROLE_USER)) {
            User user = (User) auth.getPrincipal();
            user.setSignedOut(false);
            userService.save(user);
            claims.put("fullname", user.getName());
            claims.put("email", user.getEmail());
            String jwtToken = jwtProvider.generateToken(claims, user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .role(user.getRole())
                    .build();
        } else if (auth.getAuthorities().contains(ROLE_SELLER)) {
            Seller seller = (Seller) auth.getPrincipal();
            sellerService.save(seller);
            claims.put("fullname", seller.getName());
            claims.put("email", seller.getEmail());
            String jwtToken = jwtProvider.generateToken(claims, seller);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .role(seller.getRole())
                    .build();
        }
        throw new BadCredentialsException("Invalid username or password");
    }

    @Override
    public void signOut(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user.setSignedOut(true);
        user.setLastSignOut(LocalDateTime.now());
        userService.save(user);
    }
}
