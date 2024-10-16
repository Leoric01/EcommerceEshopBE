package com.leoric.ecommerceshopbe.services.nointerface;

import com.leoric.ecommerceshopbe.models.Cart;
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
import com.leoric.ecommerceshopbe.services.interfaces.UserService;
import com.leoric.ecommerceshopbe.services.interfaces.VerificationCodeService;
import com.leoric.ecommerceshopbe.utils.OtpUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.leoric.ecommerceshopbe.models.constants.USER_ROLE.ROLE_USER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final VerificationCodeService verificationCodeService;
    private final UserService userService;
    private final EmailService emailService;


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
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
                .role(ROLE_USER)
                .build();
        user.setEnabled(true);
        user = userService.save(user);
//        sendValidationEmail(user);
        Cart cart = new Cart();
        cart.setUser(user);
        cartRepository.save(cart);
    }

    //    TODO this doesn't work, check difference
//    @Override
//    public void sentLoginOtp(@Valid VerificationCodeReq req) {
//        String SIGNING_PREFIX = "signing_";
//        String email = req.getEmail();
//        if (email.startsWith(SIGNING_PREFIX)) {
//            email = email.substring(SIGNING_PREFIX.length());
//            User user = userService.findByEmail(email);
//            boolean isExists = verificationCodeService.existsByEmail(email);
//            if (isExists) {
//                verificationCodeService.deleteByEmail(email);
//            }
//            String otp = OtpUtil.generateOtp(length);
//            VerificationCode verificationCode = new VerificationCode();
//            verificationCode.setEmail(email);
//            verificationCode.setOtp(otp);
//            verificationCodeService.save(verificationCode);
//            user.setVerificationCode(verificationCode);
//            userService.save(user);
//            String subject = "LEORIC ESHOP OTP verification code";
//            emailService.sendVerificationEmail(email, user.getName(), subject, otp);
//        }
//    }
    @Override
    public void sentLoginOtp(@Valid VerificationCodeReq req) {
        String SIGNING_PREFIX = "signing_";
        String email = req.getEmail();
        if (email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());

            // Fetch the user by email
            User user = userService.findByEmail(email);

            // Check if a VerificationCode already exists for this email
            boolean isExists = verificationCodeService.existsByEmail(email);
            if (isExists) {
                verificationCodeService.deleteByEmail(email); // Delete existing OTP
            }

            // Generate new OTP
            String otp = OtpUtil.generateOtp(length);

            // Create and associate VerificationCode with User
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setEmail(email);
            verificationCode.setOtp(otp);
            verificationCode.setUser(user); // Associate the VerificationCode with the User

            // Save VerificationCode (this will also save the user_id in the VerificationCode table)
            verificationCodeService.save(verificationCode);

            // Set the verification code on the User object and save the User
            user.setVerificationCode(verificationCode);
            userService.save(user); // Save the user again (optional, but keeps consistency)

            // Send the OTP email
            String subject = "LEORIC ESHOP OTP verification code";
            emailService.sendVerificationEmail(email, user.getName(), subject, otp);
        }
    }


    @Override
    public UserDto setupPwFromOtp(SetupPwFromOtpReq req) throws BadRequestException {
        VerificationCode verificationCode = verificationCodeService.findByEmail(req.getEmail());
        User user = verificationCode.getUser();

        if (!user.getVerificationCode().getOtp().equals(req.getOtp())) {
            throw new BadRequestException("Invalid OTP");
        }
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        User savedUser = userService.save(user);
        return new UserDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getMobile(),
                savedUser.getRole().name()
        );
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest req) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        Map<String, Object> claims = new HashMap<>();
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
    }

    @Override
    public void signOut(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        user.setSignedOut(true);
        user.setLastSignOut(LocalDateTime.now());
        userService.save(user);
    }
}
