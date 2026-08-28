package com.aeropelican.apigateway.client.userclients;

import com.aeropelican.apigateway.client.dto.ApiResponse;
import com.aeropelican.apigateway.client.dto.AuthUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

//JWT - Java Web Token

//RBAC - Role Based Acccess Controll
@FeignClient(name="user-service")
public interface UserClient {

    @GetMapping("/api/v1/users/email")
    ApiResponse<AuthUser> findByEmail(@RequestParam("email") String email);
}
