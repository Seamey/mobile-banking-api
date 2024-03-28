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
@Table(name="roles")
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToOne (mappedBy = "roles")
    private RolesAuthority rolesAuthority;

    // relationship between user and roles
    @OneToMany
    private List<User> users;

}
