package com.example.TaskManagementSystem.service.user;

import com.example.TaskManagementSystem.dto.user.UpdateRoleDTO;
import com.example.TaskManagementSystem.dto.user.UserDTO;
import com.example.TaskManagementSystem.dto.user.UserPageResponse;
import com.example.TaskManagementSystem.dto.user.UserRequestDTO;
import com.example.TaskManagementSystem.form.UserFilterForm;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    // Get paginated user with filters
    UserPageResponse getAllUsers(UserFilterForm form, Pageable pageable);

    // Get a user by ID
    UserDTO getUserById(Long id);

    // Create a new user
    UserDTO createUser(UserRequestDTO userRequestDTO);

    // Update an existing user
    UserDTO updateUser(Long id, UserRequestDTO userRequestDTO);

    // Delete a user by ID
    void deleteUser(Long id);

    // Update a user's role
    void updateUserRole(Long id, UpdateRoleDTO updateRoleDTO);

}
