package com.aeropelican.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) {
        security.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/", "/ui/login").permitAll()
                                        .anyRequest().authenticated()
                )
                .formLogin(form ->
                    form.loginPage("/ui/login")
                            .loginProcessingUrl("/ui/login")
                            .defaultSuccessUrl("/ui/home", true)
                            .permitAll()
                );
        return security.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {

        // Integrate database
        // Fetch datafrom database and load into UserDetails


        UserDetails userDetails = User
                .withUsername("Hippo")
                .password("{noop}Hippo123")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
    }
}
