package com.example.TaskManagementSystem.security;

import com.example.TaskManagementSystem.entity.ProjectMember;
import com.example.TaskManagementSystem.entity.ProjectRole;
import com.example.TaskManagementSystem.entity.Task;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.repository.IProjectMemberRepository;
import com.example.TaskManagementSystem.repository.ITaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("taskSecurity")
@RequiredArgsConstructor
public class TaskSecurity {

    private final IProjectMemberRepository projectMemberRepository;
    private final ITaskRepository taskRepository;


    // MEMBER in a project can create task
    public boolean canCreateTask(Long projectId, String username) {
        projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this project"));
        return true;
    }


    // OWNER or TASK CREATOR or ASSIGNEE
    @Transactional
    public boolean canUpdateTask(Long projectId, Long taskId, String username) {

        ProjectMember pm = projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this project"));

        Task task = taskRepository
                .findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // OWNER
        if (pm.getRole() == ProjectRole.OWNER) {
            return true;
        }

        // TASK CREATOR
        if (task.getCreatedBy() != null && task.getCreatedBy().getUsername().equals(username)) {
            return true;
        }

        // ASSIGNEE
        Optional<String> assigneeUsernameOpt = taskRepository.findAssigneeUsername(taskId, projectId);
        if (assigneeUsernameOpt.isPresent()) {
            String assigneeUsername = assigneeUsernameOpt.get();
            return assigneeUsername.equals(username);
        }

        return false;
    }


    // OWNER or TASK CREATOR can delete task
    @Transactional
    public boolean canDeleteTask(Long projectId, Long taskId, String username) {

        ProjectMember pm = projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() -> new ResourceNotFoundException("You are not a member of this project"));

        Task task = taskRepository
                .findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // OWNER
        if (pm.getRole() == ProjectRole.OWNER) {
            return true;
        }

        // TASK CREATOR
        if (task.getCreatedBy() != null && task.getCreatedBy().getUsername().equals(username)) {
            return true;
        }

        throw new ResourceNotFoundException("You don't have permission to delete this task");
    }
}
