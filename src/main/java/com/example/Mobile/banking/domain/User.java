package com.example.Mobile.banking.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDate dob;
    @Column(nullable = false)
    private Integer pin; // store 4 digit
    @Column(unique = true, nullable = false, length = 30)
    private String phoneNumber;
    private String profileImage;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String ConfirmedPassword;
    @Column(unique = true, nullable = false)
    private String nationalCardId;
    @Column(length = 100)
    private String cityOrProvince;

    @Column(length = 100)
    private String khanOrDistrict;

    @Column(length = 100)
    private String sangkatOrCommune;

    @Column(length = 100)
    private String village;

    @Column(length = 100)
    private String street;

    @Column(length = 100)
    private String employeeType;

    @Column(length = 100)
    private String position;

    @Column(length = 100)
    private String companyName;

    @Column(length = 100)
    private String mainSourceOfIncome;

    private BigDecimal monthlyIncomeRange;
private LocalDate createAt;
    private Boolean isDeleted; // manage delete status (admin want to disable or remove an account)
    private Boolean isBlocked; // manage block status (when there is bad action happened)

    // join with entity account
    @OneToMany(mappedBy = "user")  // one user has many acc
    private List<UserAccount> userAccountList;
    // relationship between user and roles
//    @ManyToMany(cascade = CascadeType.ALL)// use when roles (dynamic ) null
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id",
                    referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id",
                    referencedColumnName ="id"))

    private List<Roles> roles;
}
