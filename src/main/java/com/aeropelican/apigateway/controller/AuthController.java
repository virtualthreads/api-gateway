package com.aeropelican.apigateway.controller;

import com.aeropelican.apigateway.client.dto.ApiResponse;
import com.aeropelican.apigateway.client.dto.AuthUser;
import com.aeropelican.apigateway.client.dto.LoginRequest;
import com.aeropelican.apigateway.client.dto.LoginResponse;
import com.aeropelican.apigateway.client.userclients.UserClient;
import com.aeropelican.apigateway.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserClient userClient;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserClient userClient,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userClient = userClient;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        AuthUser user = userClient.findByEmail(request.email()).getData();
        String token = jwtService.generateToken(user.userId(), user.email(), user.role());

        LoginResponse response = new LoginResponse(
                user.userId(), user.email(), user.role(), token);

        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }
}
