package com.example.TaskManagementSystem.service.project;

import com.example.TaskManagementSystem.dto.project.AddMemberRequestDTO;
import com.example.TaskManagementSystem.dto.project.ProjectDTO;
import com.example.TaskManagementSystem.dto.project.ProjectRequestDTO;
import com.example.TaskManagementSystem.entity.Project;
import com.example.TaskManagementSystem.entity.ProjectMember;
import com.example.TaskManagementSystem.entity.ProjectRole;
import com.example.TaskManagementSystem.entity.User;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.mapper.ProjectMapper;
import com.example.TaskManagementSystem.repository.IProjectMemberRepository;
import com.example.TaskManagementSystem.repository.IProjectRepository;
import com.example.TaskManagementSystem.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProjectService implements IProjectService{
    private final IProjectRepository projectRepository;
    private final IProjectMemberRepository projectMemberRepository;
    private final IUserRepository userRepository;

    private final ProjectMapper projectMapper;


    @Override
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projectMapper.toDTOs(projects);
    }

    @Override
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Project not found"));
        return projectMapper.toDTO(project);
    }

    @Override
    public ProjectDTO createProject(ProjectRequestDTO dto, String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        Project project = projectMapper.toEntity(dto);
        project.setCreatedBy(user);

        Project savedProject = projectRepository.save(project);

        ProjectMember owner = new ProjectMember();
        owner.setProject(savedProject);
        owner.setUser(user);
        owner.setRole(ProjectRole.OWNER);

        projectMemberRepository.save(owner);

        return projectMapper.toDTO(savedProject);
    }

    public List<ProjectDTO> getMyProjects(String username) {
        List<Project> projects = projectRepository.findAllProjectsByUsername(username);
        return projectMapper.toDTOs(projects);
    }

    @Override
    public ProjectDTO updateProject(Long id, ProjectRequestDTO dto) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        project.setName(dto.getName());
        project.setDescription(dto.getDescription());

        return projectMapper.toDTO(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        projectRepository.delete(project);
    }



}
