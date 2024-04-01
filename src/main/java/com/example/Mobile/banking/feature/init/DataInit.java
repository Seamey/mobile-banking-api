package com.example.Mobile.banking.feature.init;

import com.example.Mobile.banking.domain.Role;
import com.example.Mobile.banking.feature.user.RolesRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {

    private final RolesRepository rolesRepository;

    @PostConstruct
    void init() {

        // Auto generate role (USER, CUSTOMER, STAFF, ADMIN)
        if (rolesRepository.count() < 1) {
            Role user = new Role();
            user.setName("USER");

            Role customer = new Role();
            customer.setName("CUSTOMER");

            Role staff = new Role();
            staff.setName("STAFF");

            Role admin = new Role();
            admin.setName("ADMIN");

            rolesRepository.saveAll(
                    List.of(user, customer, staff, admin)
            );
        }

    }

}

