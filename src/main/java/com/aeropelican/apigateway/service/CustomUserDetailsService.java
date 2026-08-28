package com.aeropelican.apigateway.service;

import com.aeropelican.apigateway.client.dto.ApiResponse;
import com.aeropelican.apigateway.client.dto.AuthUser;
import com.aeropelican.apigateway.client.userclients.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting to fetch user details for email: {}", email);
        ApiResponse<AuthUser> user = userClient.findByEmail(email);
        AuthUser authUser = user.getData();

        UserDetails userDetails = User
                .withUsername(authUser.email())
                .password(authUser.hashedPassword())
                .build();
        return userDetails;
    }
}
