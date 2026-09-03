package com.aeropelican.apigateway.controller;

import com.aeropelican.apigateway.model.AuthRequest;
import com.aeropelican.apigateway.model.AuthResponse;
import com.aeropelican.apigateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.email(), authRequest.password())
        );
        UserDetails userResponse = (UserDetails) authentication.getPrincipal();
        //UserDetails userResponse = userDetailsService.loadUserByUsername(authRequest.email());
        String token = jwtService.generateToken(userResponse);
        log.info("Generated token: {}", token);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
