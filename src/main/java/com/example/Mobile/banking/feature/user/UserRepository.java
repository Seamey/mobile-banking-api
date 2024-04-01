package com.example.Mobile.banking.feature.user;

import com.example.Mobile.banking.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByStudentIdCard(String studentIdCard);
    boolean existsByNationalCardId(String nationalIdCard);
    //@Query(value = "SELECT * FROM users WHERE uuid = ?1", nativeQuery = true)
    @Query("SELECT u FROM User AS u WHERE u.uuid = :uuid")
    Optional<User> findByUuid(String uuid);

    boolean existsByUuid(String uuid);

    @Modifying
    @Query("UPDATE User AS u SET u.isBlocked = TRUE WHERE u.uuid = ?1")
    void blockByUuid(String uuid);


}
