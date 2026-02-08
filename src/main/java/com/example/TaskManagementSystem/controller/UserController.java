package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.user.UpdateRoleDTO;
import com.example.TaskManagementSystem.dto.user.UserDTO;
import com.example.TaskManagementSystem.dto.user.UserPageResponse;
import com.example.TaskManagementSystem.dto.user.UserRequestDTO;
import com.example.TaskManagementSystem.form.UserFilterForm;
import com.example.TaskManagementSystem.service.user.IUserService;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/users")
@Tag(name = "User API", description = "APIs for users and roles")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @Operation(summary = "Get all users", description = "Admin can get paginated list of users")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<UserPageResponse> getAllUsers(UserFilterForm form, @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.ok(userService.getAllUsers(form, pageable));
    }

    @Operation(summary = "Get user by ID", description = "Only admin can get user by ID")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Create a new user", description = "Only admin create a new user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserRequestDTO userRequestDTO){
        UserDTO createUserDTO = userService.createUser(userRequestDTO);
        return new ResponseEntity<>(createUserDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Update user info")
    @PutMapping("{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDTO userRequestDTO){
        UserDTO updateUserDTO = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.ok(updateUserDTO);
    }

    @Operation(summary = "Update user role", description = "Only admin can update user role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("{id}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleDTO updateRoleDTO,
                                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            userService.updateUserRole(id, updateRoleDTO);
            return ResponseEntity.ok("User role updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Delete user", description = "Only admin can delete user")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>("Id: "+id+" is deleted", HttpStatus.OK);
    }
}