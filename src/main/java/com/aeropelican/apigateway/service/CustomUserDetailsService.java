package com.aeropelican.apigateway.service;

import com.aeropelican.commonsservice.user.clients.UserServiceClient;
import com.aeropelican.commonsservice.user.dto.response.ApiResponse;
import com.aeropelican.commonsservice.user.dto.response.RoleResponse;
import com.aeropelican.commonsservice.user.dto.response.UserAuthResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        try {
            ApiResponse<UserAuthResponse> response = userServiceClient.getUserByEmailForAuth(email);
            if (response == null || response.getData() == null) {
                throw new UsernameNotFoundException("User not found: " + email);
            }
            UserAuthResponse user = response.getData();
            List<SimpleGrantedAuthority> authorities = user.roles().stream()
                    .map(RoleResponse::roleName)
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            return new AuthenticatedUser(user.userId(), user.email(), user.passwordHash(), authorities);
        } catch (FeignException.Unauthorized | FeignException.NotFound e) {
            throw new UsernameNotFoundException("User not found: " + email, e);
        }
    }
}
