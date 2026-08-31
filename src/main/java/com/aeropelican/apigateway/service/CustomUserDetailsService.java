package com.aeropelican.apigateway.service;

import com.aeropelican.commonsservice.user.clients.UserServiceClient;
import com.aeropelican.commonsservice.user.dto.response.ApiResponse;
import com.aeropelican.commonsservice.user.dto.response.UserAuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting to fetch user details for email: {}", email);
        ApiResponse<UserAuthResponse> response = userServiceClient.getUserByEmailForAuth(email);
        UserAuthResponse userAuthResponse = response.getData();

        UserDetails userDetails = User
                .withUsername(userAuthResponse.email())
                .password(userAuthResponse.passwordHash())
                .build();
        return userDetails;
    }
}
