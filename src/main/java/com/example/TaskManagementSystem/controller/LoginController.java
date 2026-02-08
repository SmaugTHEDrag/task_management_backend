package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.auth.LoginResponse;
import com.example.TaskManagementSystem.form.LoginForm;
import com.example.TaskManagementSystem.utils.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "API for user login")
public class LoginController {
    private final AuthenticationManager authenticationManager;

    // authenticates the user using provided login credentials and returns a JWT token
    @Operation(summary = "User login")
    @PostMapping
    public ResponseEntity<Object> login(@RequestBody @Valid LoginForm loginForm) {
        // authenticate user credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getLogin(), loginForm.getPassword())
        );

        // set authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate JWT token
        String jwt = JwtUtils.generateJwt(authentication);

        // get username and role from authentication
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);

        // build response
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwt);
        loginResponse.setLogin(username);
        loginResponse.setRole(role);

        return ResponseEntity.ok(loginResponse);
    }
}
