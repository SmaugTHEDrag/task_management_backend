package com.example.TaskManagementSystem.service.project;

import com.example.TaskManagementSystem.dto.project.AddMemberRequestDTO;
import com.example.TaskManagementSystem.dto.project.ProjectDTO;
import com.example.TaskManagementSystem.dto.project.ProjectRequestDTO;
import com.example.TaskManagementSystem.entity.User;

import java.util.List;

public interface IProjectService {
    List<ProjectDTO> getAllProjects();
    ProjectDTO getProjectById(Long id);
    ProjectDTO createProject(ProjectRequestDTO projectRequestDTO, String username);
    List<ProjectDTO> getMyProjects(String username);
    ProjectDTO updateProject(Long id, ProjectRequestDTO projectRequestDTO);
    void deleteProject(Long id);
}
