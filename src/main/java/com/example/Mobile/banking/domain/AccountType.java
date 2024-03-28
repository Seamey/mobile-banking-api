package com.example.Mobile.banking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="account_Type")
public class AccountType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true,nullable = false ,length = 100)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Boolean isDeleted;

    // that Bidirectional (relationship account between account-type)
    // account-type has a lot of account
    @OneToMany (mappedBy = "accountType")
    private List<Account> accounts;
}
