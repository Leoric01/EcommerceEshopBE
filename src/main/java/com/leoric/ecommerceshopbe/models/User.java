package com.leoric.ecommerceshopbe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.leoric.ecommerceshopbe.models.constants.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class User implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String mobile;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    @Builder.Default
    private boolean enabled = true;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany
    @JsonIgnore
//    @JoinTable(
//            name = "user_coupon",
//            joinColumns = @JoinColumn(name = "user_id"),
//            inverseJoinColumns = @JoinColumn(name = "coupon_id")
//    )
    private Set<Coupon> usedCoupons;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;

    @OneToMany(mappedBy = "customer")
    private List<Transaction> transactions;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wishlist wishlist;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private VerificationCode verificationCode;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.role.toString()));
    }

    @Builder.Default
    private boolean isSignedOut = false;

    private LocalDateTime lastSignOut;

    public boolean isSignedOut() {
        return isSignedOut;
    }

    public void setSignedOut(boolean signedOut) {
        isSignedOut = signedOut;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }


    @Override
    public String getName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}

