package com.example.Mobile.banking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 9)
    private String actNo;
    @Column(unique = true, nullable = false, length = 9)
    private String actName;

    @Column(length = 100)
    private String alias;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private BigDecimal transferLimit;

    // Account has type
    //default join by column
    @ManyToOne   // account has an account-type
                // account-type has a lot of account
    private AccountType accountType;

    // join with entity users
    @OneToMany(mappedBy = "account") // join with entity users
//    @JoinTable(name="user-accounts")  // user-account is the name me dek oy
    private List<UserAccount> userAccounts;
    @OneToOne  // relationship between card and account
    private Card card;
    private Boolean isHidden; // uses to hide account on mobile app
}
