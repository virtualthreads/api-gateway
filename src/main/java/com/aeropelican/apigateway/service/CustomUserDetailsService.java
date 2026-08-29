package com.aeropelican.apigateway.service;

import com.aeropelican.apigateway.client.dto.ApiResponse;
import com.aeropelican.apigateway.client.dto.AuthUser;
import com.aeropelican.apigateway.client.userclients.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting to fetch user details for email: {}", email);
        ApiResponse<AuthUser> response = userClient.findByEmail(email);
        AuthUser authUser = response.getData();

        if (authUser == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        List<SimpleGrantedAuthority> authorities = authUser.role() == null
                ? List.of()
                : List.of(new SimpleGrantedAuthority("ROLE_" + authUser.role()));

        return User.withUsername(authUser.email())
                .password(authUser.hashedPassword())
                .authorities(authorities)
                .build();
    }
}
