package com.example.Mobile.banking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String uuid;
    @Column(length = 50)
    private String name;
    @Column(length = 8)
    private String gender;
    @Column(unique = true)
    private String oneSignalId;
    @Column(unique = true)
    private String studentIdCard;

    private Boolean isDeleted;
    private Boolean isStudent;

    // join with entity account
    @OneToMany(mappedBy = "user")  // one user has many acc
    private List<UserAccount> accounts;
    // relationship between user and roles
    @ManyToOne
    private Roles roles;
}
