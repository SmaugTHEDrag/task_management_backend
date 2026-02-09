package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.user.UpdateRoleDTO;
import com.example.TaskManagementSystem.dto.user.UserDTO;
import com.example.TaskManagementSystem.dto.user.UserPageResponse;
import com.example.TaskManagementSystem.dto.user.UserRequestDTO;
import com.example.TaskManagementSystem.form.UserFilterForm;
import com.example.TaskManagementSystem.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "2. Users", description = "APIs for managing users and roles (Admin only)")
public class UserController {

    private final IUserService userService;

    @Operation(summary = "Get all users", description = "Get paginated list of users with filter options (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<UserPageResponse> getAllUsers(UserFilterForm form,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC
            ) Pageable pageable){
        return ResponseEntity.ok(userService.getAllUsers(form, pageable));
    }


    @Operation(summary = "Get user by ID", description = "Get user details by user ID (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id)
    {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @Operation(summary = "Create a new user", description = "Create a new user account (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserRequestDTO userRequestDTO)
    {
        UserDTO createdUser = userService.createUser(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    @Operation(summary = "Update user information", description = "Update user profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
    }


    @Operation(summary = "Update user role", description = "Update role of a user (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleDTO updateRoleDTO,
            BindingResult bindingResult)
    {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        userService.updateUserRole(id, updateRoleDTO);
        return ResponseEntity.ok("User role updated successfully");
    }


    @Operation(summary = "Delete user", description = "Delete a user by ID (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User with id " + id + " has been deleted");
    }
}
