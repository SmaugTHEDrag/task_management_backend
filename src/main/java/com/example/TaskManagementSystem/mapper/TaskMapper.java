package com.example.TaskManagementSystem.mapper;

import com.example.TaskManagementSystem.dto.task.TaskDTO;
import com.example.TaskManagementSystem.dto.task.TaskRequestDTO;
import com.example.TaskManagementSystem.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "assignee", source = "assignee.username")
    @Mapping(target = "createdBy", source = "createdBy.username")
    TaskDTO toDTO(Task task);

    List<TaskDTO> toDTOs(List<Task> tasks);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "assignee", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Task toEntity(TaskRequestDTO dto);
}
