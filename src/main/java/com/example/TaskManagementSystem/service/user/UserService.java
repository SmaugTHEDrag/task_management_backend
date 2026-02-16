package com.example.TaskManagementSystem.service.user;

import com.example.TaskManagementSystem.dto.user.*;
import com.example.TaskManagementSystem.entity.User;
import com.example.TaskManagementSystem.entity.UserRole;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.form.UserFilterForm;
import com.example.TaskManagementSystem.mapper.UserMapper;
import com.example.TaskManagementSystem.repository.IUserRepository;
import com.example.TaskManagementSystem.specification.UserSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService{
    private final IUserRepository userRepository;

    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    // Get users with pagination and filter
    @Override
    public UserPageResponse getAllUsers(UserFilterForm form, Pageable pageable) {
        Specification<User> where = UserSpecification.buildWhere(form);
        Page<User> users = userRepository.findAll(where,pageable);
        Page<UserDTO> userDTOS = users.map(userMapper::toDTO);

        return new UserPageResponse(
                userDTOS.getContent(),
                userDTOS.getNumber(),
                userDTOS.getTotalElements(),
                userDTOS.getTotalPages(),
                userDTOS.getSize(),
                userDTOS.isLast(),
                userDTOS.isFirst()
        );
    }

    // Get a user by ID
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        return userMapper.toDTO(user);
    }

    // Creates new user.
    @Override
    public UserDTO createUser(UserRequestDTO userRequestDTO) {
        User user = userMapper.toEntity(userRequestDTO);
        User savedUser = userRepository.save(user);

        return userMapper.toDTO(savedUser);
    }

    // Update user information
    @Override
    public UserDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id: "+id));

        userMapper.updateEntityFromDTO(userRequestDTO, existingUser);
        User updatedUser = userRepository.save(existingUser);

        return userMapper.toDTO(updatedUser);
    }

    // Deletes user by ID
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Update user role
    @Override
    public void updateUserRole(Long id, UpdateRoleDTO updateRoleDTO) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        User user = optionalUser.get();
        user.setRole(UserRole.valueOf(updateRoleDTO.getRole()));
        userRepository.save(user);
    }

    // Search users by username (for adding project members)
    @Override
    public java.util.List<UserDTO> searchUsersByUsername(String query, int limit) {
        if (query == null || query.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }

        UserFilterForm form = new UserFilterForm();
        form.setUsernameSearch(query.trim());
        
        Specification<User> where = UserSpecification.buildWhere(form);
        Pageable pageable = org.springframework.data.domain.PageRequest.of(0, limit);
        Page<User> users = userRepository.findAll(where, pageable);
        
        return users.getContent().stream()
                .map(userMapper::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public UserDTO getCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return userMapper.toDTO(user);
    }

    @Override
    public void changePassword(String username, ChangePasswordDTO dto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}
