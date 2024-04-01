package com.example.Mobile.banking.feature.user;

import com.example.Mobile.banking.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByName(String name);
}
