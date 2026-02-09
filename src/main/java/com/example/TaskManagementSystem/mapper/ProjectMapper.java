package com.example.TaskManagementSystem.mapper;

import com.example.TaskManagementSystem.dto.project.ProjectDTO;
import com.example.TaskManagementSystem.dto.project.ProjectRequestDTO;
import com.example.TaskManagementSystem.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(target = "createdBy", source = "createdBy.username")
    ProjectDTO toDTO(Project project);

    List<ProjectDTO> toDTOs(List<Project> projects);

    @Mapping(target = "createdBy", ignore = true)
    Project toEntity(ProjectRequestDTO projectRequestDTO);
}
