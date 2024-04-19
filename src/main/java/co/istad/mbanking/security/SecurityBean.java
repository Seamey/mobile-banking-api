package co.istad.mbanking.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityBean
{
    // mean encrypassword
    @Bean
    // that the inface
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
