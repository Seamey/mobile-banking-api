package co.istad.mbanking.features.auth;

import co.istad.mbanking.features.auth.dto.LoginRequest;
import co.istad.mbanking.features.auth.dto.AuthResponse;
import co.istad.mbanking.features.auth.dto.RefreshTokenRequest;
import co.istad.mbanking.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor


public class AuthServiceImpl implements AuthService{
    private  final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private  final JwtEncoder jwtEncoder;

    private JwtEncoder refreshJwtEncoder;
    @Qualifier("refreshJwtEncoder")
    @Autowired
    public void setJwtEncoder(JwtEncoder refreshJwtEncoder) {
        this.refreshJwtEncoder = refreshJwtEncoder;
    }



    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginRequest.phoneNumber(),
                loginRequest.password());
       auth = daoAuthenticationProvider.authenticate(auth);

       CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
       log.info(userDetails.getUsername());
       log.info(userDetails.getPassword());
       log.info(userDetails.getUser().getName());
       userDetails.getAuthorities().forEach(authenticate -> authenticate.getAuthority());

        Instant time = Instant.now();
        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority->!authority.startsWith("ROLE_"))  // filter yk tea authority
                .collect(Collectors.joining(" ")); // convert to string

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet
                .builder()
                .id(userDetails.getUsername())
                .audience(List.of( "WEB","MOBILE"))
                .subject("Access Resource")
                .issuer(userDetails.getUsername())
                .claim("scope", scope)
                .issuedAt(time)
                .expiresAt(time.plus(5, ChronoUnit.MINUTES))
                .build();

        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();


        // REFRESH TOKEN

        JwtClaimsSet refreshJwtClaimsSet = JwtClaimsSet
                .builder()
                .id(userDetails.getUsername())
                .audience(List.of( "WEB","MOBILE"))
                .subject("Refresh Resource")
                .issuer(userDetails.getUsername())
                .issuedAt(time)
                .claim("scope", scope)
                .expiresAt(time.plus(1, ChronoUnit.DAYS))
                .build();

        String refreshToken = refreshJwtEncoder.encode(JwtEncoderParameters.from(refreshJwtClaimsSet)).getTokenValue();
        log.info("refreshToken: {}",refreshToken);

        return new AuthResponse(
                "Bearer",
                accessToken,
                refreshToken

        );
    }
// not work--------------------------------------------------------
    @Override
    public AuthResponse refresh(RefreshTokenRequest refreshTokenRequest) {
        Authentication auth = new BearerTokenAuthenticationToken(
                refreshTokenRequest.refreshToken()
        );

        Instant now = Instant.now();
        auth = jwtAuthenticationProvider.authenticate(auth);
        Jwt jwt = (Jwt) auth.getPrincipal();
        log.info(jwt.getClaimAsString("scope"));
        log.info(jwt.getSubject());
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet
                .builder()
                .id(jwt.getId())
                .audience(List.of( "WEB","MOBILE"))
                .subject("Access Resource")
                .issuer(jwt.getId())
                .claim("scope", jwt.getClaimAsString("scope"))
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .build();
        String accessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();
        return new AuthResponse(
                "Bearer",
                accessToken,
                refreshTokenRequest.refreshToken() // mean that request old refresh token
        );
    }
// ------------------------------------------------------------------------------
}
