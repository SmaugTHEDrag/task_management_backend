package com.example.TaskManagementSystem.service.task;

import com.example.TaskManagementSystem.dto.task.TaskDTO;
import com.example.TaskManagementSystem.dto.task.TaskRequestDTO;
import com.example.TaskManagementSystem.entity.*;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.mapper.TaskMapper;
import com.example.TaskManagementSystem.repository.IProjectMemberRepository;
import com.example.TaskManagementSystem.repository.IProjectRepository;
import com.example.TaskManagementSystem.repository.ITaskRepository;
import com.example.TaskManagementSystem.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class TaskService implements ITaskService {

    private final ITaskRepository taskRepository;
    private final IProjectRepository projectRepository;
    private final IProjectMemberRepository projectMemberRepository;
    private final IUserRepository userRepository;
    private final TaskMapper taskMapper;

    @Override
    public TaskDTO createTask(Long projectId, TaskRequestDTO dto, String username) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        Task task = taskMapper.toEntity(dto);
        task.setProject(project);

        if (dto.getAssignee() != null && !dto.getAssignee().isBlank()) {

            User assignee = userRepository
                    .findByUsername(dto.getAssignee())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));

            projectMemberRepository
                    .findByProject_IdAndUser_Username(projectId, dto.getAssignee())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Assignee is not a member of this project"));

            task.setAssignee(assignee);
        }
        return taskMapper.toDTO(taskRepository.save(task));
    }

    @Override
    public List<TaskDTO> getTasksByProject(Long projectId) {
        return taskMapper.toDTOs(
                taskRepository.findByProject_Id(projectId)
        );
    }

    @Override
    public TaskDTO getTaskDetail(Long projectId, Long taskId) {

        Task task = taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return taskMapper.toDTO(task);
    }

    @Override
    public TaskDTO updateTask(Long projectId, Long taskId, TaskRequestDTO dto, String username) {

        Task task = taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // update basic fields (null-safe)
        if (dto.getTitle() != null) {
            task.setTitle(dto.getTitle());
        }

        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }

        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());
        }

        if (dto.getPriority() != null) {
            task.setPriority(dto.getPriority());
        }

        if (dto.getDueDate() != null) {
            if (dto.getDueDate().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Due date cannot be in the past");
            }
            task.setDueDate(dto.getDueDate());
        }

        // update assignee (username)
        if (dto.getAssignee() != null && !dto.getAssignee().isBlank()) {

            // chỉ OWNER mới được đổi assignee
            ProjectMember pm = projectMemberRepository
                    .findByProject_IdAndUser_Username(projectId, username)
                    .orElseThrow(() -> new ResourceNotFoundException("Not project member"));

            if (pm.getRole() != ProjectRole.OWNER) {
                throw new IllegalStateException("Only OWNER can change assignee");
            }

            User assignee = userRepository
                    .findByUsername(dto.getAssignee())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found"));

            projectMemberRepository
                    .findByProject_IdAndUser_Username(projectId, dto.getAssignee())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Assignee is not project member"));

            task.setAssignee(assignee);
        }

        return taskMapper.toDTO(taskRepository.save(task));
    }


    @Override
    public void deleteTask(Long projectId, Long taskId, String username) {

        Task task = taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        taskRepository.delete(task);
    }
}
