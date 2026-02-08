package com.example.TaskManagementSystem.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@Tag(name = "Auth API", description = "API for user login")
@RequiredArgsConstructor
public class LoginController {
}
