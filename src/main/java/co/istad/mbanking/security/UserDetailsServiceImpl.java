package co.istad.mbanking.security;

import co.istad.mbanking.features.user.UserRepository;
import co.istad.mbanking.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       // load user from database
        User user = userRepository.findByPhoneNumber(username)  // cuz phonenumber tobe username
                .orElseThrow(()->
                new UsernameNotFoundException("User not found "));
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUser(user);
        return customUserDetails;

    }
}
