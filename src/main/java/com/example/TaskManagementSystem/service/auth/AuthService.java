package com.example.TaskManagementSystem.service.auth;

import com.example.TaskManagementSystem.dto.user.UserDTO;
import com.example.TaskManagementSystem.entity.User;
import com.example.TaskManagementSystem.entity.UserRole;
import com.example.TaskManagementSystem.form.RegisterForm;
import com.example.TaskManagementSystem.mapper.AuthMapper;
import com.example.TaskManagementSystem.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService, UserDetailsService {

    private final IUserRepository userRepository;

    private final AuthMapper authMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDTO register(RegisterForm registerForm) {
        if (userRepository.existsByUsername(registerForm.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userRepository.existsByEmail(registerForm.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = authMapper.toUserEntity(registerForm);
        user.setPassword(passwordEncoder.encode(registerForm.getPassword()));

        // Default role is USER
        UserRole role = UserRole.USER;
        if (registerForm.getRole() != null) {
            role = UserRole.valueOf(registerForm.getRole().toUpperCase());
        }
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return authMapper.toDTO(savedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = login.contains("@")
                ? userRepository.findByEmail(login).orElseThrow(() -> new UsernameNotFoundException("Email not found"))
                : userRepository.findByUsername(login).orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole().toString())
        );
    }
}
