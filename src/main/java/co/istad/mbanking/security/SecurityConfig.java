package co.istad.mbanking.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.HttpBasicDsl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }


    // create wall for protect
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // config everything clicent request has security(.authenticated())
        httpSecurity.authorizeHttpRequests(request ->

                request  // mean this endpoint can request if has roles admin
                        //.requestMatchers("/api/v1/users/**").hasRole("admin")
                        // mean this endpoint can delete if has roles admin
                        .requestMatchers(HttpMethod.POST, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/accounts/**").hasAnyRole("STAFF","CUSTOMER","USER")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/accounts/**").hasAnyRole("STAFF","ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/v1/medias/**").hasAnyRole("STAFF","CUSTOMER")

                        .anyRequest()
                        .authenticated()
        );

        // .permitedAll() mean allow to rquest not security

        // mean make it know(create) form login and then we use default username and password
        httpSecurity.httpBasic(Customizer.withDefaults());
        // why we neeed to disable cuz
        // disable CSRF
        httpSecurity.csrf(token->token.disable());
        // change to stateless
        httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();

    }
}
