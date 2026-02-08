package com.example.TaskManagementSystem.mapper;

import com.example.TaskManagementSystem.dto.user.UserDTO;
import com.example.TaskManagementSystem.dto.user.UserRequestDTO;
import com.example.TaskManagementSystem.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDTO(User user);

    List<UserDTO> toDTOs(List<User> users);

    User toEntity(UserRequestDTO userRequestDTO);

    void updateEntityFromDTO(UserRequestDTO userRequestDTO, @MappingTarget User existingUser);
}
