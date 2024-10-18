package com.leoric.ecommerceshopbe.services.nointerface;

import com.leoric.ecommerceshopbe.handler.EmailAlreadyInUseException;
import com.leoric.ecommerceshopbe.handler.OtpVerificationException;
import com.leoric.ecommerceshopbe.models.Cart;
import com.leoric.ecommerceshopbe.models.Seller;
import com.leoric.ecommerceshopbe.models.User;
import com.leoric.ecommerceshopbe.models.VerificationCode;
import com.leoric.ecommerceshopbe.repositories.CartRepository;
import com.leoric.ecommerceshopbe.requests.SetupPwFromOtpReq;
import com.leoric.ecommerceshopbe.requests.SignInRequest;
import com.leoric.ecommerceshopbe.requests.SignupRequest;
import com.leoric.ecommerceshopbe.requests.VerificationCodeReq;
import com.leoric.ecommerceshopbe.response.AccountDetailDto;
import com.leoric.ecommerceshopbe.response.AuthenticationResponse;
import com.leoric.ecommerceshopbe.security.JwtProvider;
import com.leoric.ecommerceshopbe.services.email.EmailService;
import com.leoric.ecommerceshopbe.services.interfaces.AuthService;
import com.leoric.ecommerceshopbe.services.interfaces.SellerService;
import com.leoric.ecommerceshopbe.services.interfaces.UserService;
import com.leoric.ecommerceshopbe.services.interfaces.VerificationCodeService;
import com.leoric.ecommerceshopbe.utils.OtpUtil;
import com.leoric.ecommerceshopbe.utils.abstracts.Account;
import jakarta.transaction.Transactional;
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
import static com.leoric.ecommerceshopbe.utils.GlobalUtil.getAccountFromPrincipal;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final String SELLER_PREFIX = "seller_";
    private static final String USER_PREFIX = "user_";
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
        if (request.getEmail().startsWith(SELLER_PREFIX)) {
            if (sellerService.existsByEmail(request.getEmail().substring(SELLER_PREFIX.length()))) {
                throw new EmailAlreadyInUseException("Email is already in use");
            }
            Seller seller = new Seller();
            seller.setEmail(request.getEmail().substring(SELLER_PREFIX.length()));
            seller.setSellerName(request.getFullName());
            seller.setRole(ROLE_SELLER);
            sellerService.save(seller);
            return;
        } else if (request.getEmail().startsWith(USER_PREFIX)) {
            if (userService.existsByEmail(request.getEmail().substring(USER_PREFIX.length()))) {
                throw new EmailAlreadyInUseException("Email is already in use");
            }
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail().substring(USER_PREFIX.length()))
                    .role(ROLE_USER)
                    .build();
            Cart cart = new Cart();
            cart.setUser(userService.save(user));
            cartRepository.save(cart);
            return;
        }
        throw new BadCredentialsException("Invalid email address or role prefix");
    }

    @Override
    @Transactional
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
            String subject = "LEORIC ESHOP OTP verification code";
            emailService.sendVerificationEmail(email, seller.getName(), subject, otp);
            return;
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
            String subject = "LEORIC ESHOP OTP verification code";
            emailService.sendVerificationEmail(email, user.getName(), subject, otp);
            return;
        }
        throw new BadCredentialsException("request is from entity with neither seller nor user role");
    }


    @Override
    @Transactional
    public AccountDetailDto setupPwFromOtp(SetupPwFromOtpReq req) throws BadRequestException {
        String email;
        VerificationCode verificationCode;
        if (req.getEmail().startsWith(USER_PREFIX)) {
            email = req.getEmail().substring(USER_PREFIX.length());
            verificationCode = verificationCodeService.findByEmail(email);
            User user = verificationCode.getUser();
            if (!user.getVerificationCode().getOtp().equals(req.getOtp())) {
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
        } else if (req.getEmail().startsWith(SELLER_PREFIX)) {
            email = req.getEmail().substring(SELLER_PREFIX.length());
            verificationCode = verificationCodeService.findByEmail(email);
            Seller seller = sellerService.getSellerByEmail(email);

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
        throw new BadRequestException("Invalid email address or role prefix");
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

            if (savedUser.isSignedIn()) {
                throw new Exception("Something went wrong in user logout");
            }

        } else if (principal instanceof Seller seller) {
            seller.setSignedOut(true);
            seller.setLastSignOut(LocalDateTime.now());
            sellerService.save(seller);

            if (seller.isSignedIn()) {
                throw new Exception("Something went wrong in seller logout");
            }

        } else {
            throw new Exception("Unknown principal type, cannot sign out");
        }
    }

}
