package co.istad.mbanking.security;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSelector;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig<keyPairGenerator> {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;



// access token
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
// refresh token // use when request fresh token to get access token
    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(@Qualifier("refreshJwtDecoder") JwtDecoder refreshJwtDecoder) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(refreshJwtDecoder);
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
//                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyAuthority("SCOPE_user:read")
                        .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasAnyAuthority("SCOPE_user:read")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasAuthority("SCOPE_user:write")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasAuthority("SCOPE_user:write")
                        .requestMatchers(HttpMethod.GET,"/api/v1/users/**").hasAnyAuthority("SCOPE_ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/v1/accounts/**").hasAuthority("SCOPE_account:read")
                        .requestMatchers(HttpMethod.DELETE,"/api/v1/accounts/**").hasAuthority("SCOPE_account:write")
                        .requestMatchers(HttpMethod.GET,"/api/v1/accounts/**").hasAnyAuthority("SCOPE_ADMIN", "SCOPE_account:read")

                        .requestMatchers(HttpMethod.POST,"api/v1/auth/**").permitAll()

                        .anyRequest()
                        .authenticated()
        );

        // .permitedAll() mean allow to rquest not security

        // mean make it know(create) form login and then we use default username and password
//        httpSecurity.httpBasic(Customizer.withDefaults());
        // mechanism jwt
        httpSecurity.oauth2ResourceServer(
                jwt->jwt.jwt(Customizer.withDefaults()));

        // why we neeed to disable cuz
        // disable CSRF
        httpSecurity.csrf(token->token.disable());
        // change to stateless
        httpSecurity.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return httpSecurity.build();

    }

    // REFRESH TOKEN ==============================================
    // for some senario doesn't have this Bean cuz the generate public key , so must be write code for load that public key
    // keyPair = generate public key
    @Bean("refreshKeyPair")
    KeyPair refreshKeyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean("refreshRsaKey")
    RSAKey refreshRsaKey(@Qualifier("refreshKeyPair") KeyPair keyPair){
        return  new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean("refreshJwkSource")
    JWKSource<SecurityContext> refreshJwkSource(@Qualifier("refreshRsaKey") RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new JWKSource<SecurityContext>() {
            @Override
            public List<JWK> get(JWKSelector jwkSelector, SecurityContext securityContext) throws KeySourceException {
                return jwkSelector.select(jwkSet);
            }
        };
    }
    @Bean("refreshJwtEncoder")
    JwtEncoder refreshJwtEncoder(@Qualifier("refreshJwkSource") JWKSource<SecurityContext> jwkSource) {
        return  new NimbusJwtEncoder(jwkSource);
    }

    @Bean("refreshJwtDecoder")
    JwtDecoder refreshJwtDecoder(@Qualifier("refreshRsaKey") RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }

    // JWT ACCESS TOKEN ===========================================================
    @Primary
    @Bean
    KeyPair keyPair() {
        try {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Primary
    @Bean
    RSAKey rsaKey(KeyPair keyPair){
        return  new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Primary
    @Bean
    JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new JWKSource<SecurityContext>() {
            @Override
            public List<JWK> get(JWKSelector jwkSelector, SecurityContext securityContext) throws KeySourceException {
                return jwkSelector.select(jwkSet);
            }
        };
    }
    @Primary
    @Bean
    // encode for public key
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return  new NimbusJwtEncoder(jwkSource);
    }

    @Primary
    @Bean
    // decoder for private key
    JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey()) // but need to put public key too, make sure that public key 's this private key
                .build();
    }
}
