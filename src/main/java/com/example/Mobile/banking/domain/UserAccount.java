package com.example.Mobile.banking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// the way to customer table relationship
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="user_account")
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Column(unique = true)
    @ManyToOne
    private User user;
    @ManyToOne
    private Account account;
    private Boolean isDeleted;
    private LocalDateTime createAt;


}
