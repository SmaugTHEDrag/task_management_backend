package com.example.TaskManagementSystem.service.auth;

import com.example.TaskManagementSystem.dto.user.UserDTO;
import com.example.TaskManagementSystem.form.RegisterForm;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IAuthService extends UserDetailsService {

    UserDTO register(RegisterForm registerForm);
    
}
