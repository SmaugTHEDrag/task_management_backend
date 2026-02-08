package com.example.TaskManagementSystem.mapper;

import com.example.TaskManagementSystem.dto.user.UserDTO;
import com.example.TaskManagementSystem.entity.User;
import com.example.TaskManagementSystem.form.RegisterForm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    User toUserEntity(RegisterForm registerForm);

    UserDTO toDTO(User savedUser);
}
