package com.example.TaskManagementSystem.mapper;

import com.example.TaskManagementSystem.dto.task.TaskCommentDTO;
import com.example.TaskManagementSystem.entity.TaskComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskCommentMapper {

    @Mapping(source = "user.username", target = "username")
    TaskCommentDTO toDTO(TaskComment entity);
}
