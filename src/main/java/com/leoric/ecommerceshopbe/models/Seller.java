package com.leoric.ecommerceshopbe.models;

import com.leoric.ecommerceshopbe.models.constants.AccountStatus;
import com.leoric.ecommerceshopbe.models.constants.USER_ROLE;
import com.leoric.ecommerceshopbe.models.embeded.BankDetails;
import com.leoric.ecommerceshopbe.models.embeded.BusinessDetails;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sellerName;

    private String mobile;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    //    @JoinColumn(name = "address_id")
    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress = new Address();

    private String GSTIN;

    private USER_ROLE role = USER_ROLE.ROLE_SELLER;

    private boolean isEmailVerified = false;
    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;

}
