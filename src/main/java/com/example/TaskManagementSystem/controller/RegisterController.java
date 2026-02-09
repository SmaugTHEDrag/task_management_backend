package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.user.UserDTO;
import com.example.TaskManagementSystem.form.RegisterForm;
import com.example.TaskManagementSystem.service.auth.IAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "1. Authentication", description = "APIs for user registration and authentication")
public class RegisterController {

    private final IAuthService userService;

    @Operation(summary = "Register a new user", description = "Create a new user account with validation (Username and email must be unique)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or user already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterForm registerForm,
                                          BindingResult bindingResult)
    {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        try {
            UserDTO createdUser = userService.register(registerForm);
            return ResponseEntity.ok(createdUser);
        } catch (RuntimeException e) {
            // thrown when username or email already exists
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}
