package com.leoric.ecommerceshopbe.security.auth;

import com.leoric.ecommerceshopbe.models.Seller;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otp;
    private String email;

    //    @JoinColumn(name = "user_id")
    @OneToOne
    private User user;

    //    @JoinColumn(name = "seller_id")
    @OneToOne
    private Seller seller;
}
