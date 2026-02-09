package com.example.TaskManagementSystem.security;

import com.example.TaskManagementSystem.entity.ProjectMember;
import com.example.TaskManagementSystem.entity.ProjectRole;
import com.example.TaskManagementSystem.entity.Task;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.repository.IProjectMemberRepository;
import com.example.TaskManagementSystem.repository.ITaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("taskSecurity")
@RequiredArgsConstructor
public class TaskSecurity {

    private final IProjectMemberRepository projectMemberRepository;
    private final ITaskRepository taskRepository;

    // ===== CREATE TASK =====
    // OWNER hoặc MEMBER đều được tạo
    public boolean canCreateTask(Long projectId, String username) {
        projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("You are not a member of this project"));
        return true;
    }

    // ===== UPDATE TASK =====
    // OWNER hoặc ASSIGNEE
    public boolean canUpdateTask(Long projectId, Long taskId, String username) {

        ProjectMember pm = projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("You are not a member of this project"));

        Task task = taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        // OWNER luôn được
        if (pm.getRole() == ProjectRole.OWNER) {
            return true;
        }

        // Assignee được update task của mình
        return task.getAssignee() != null
                && task.getAssignee().getUsername().equals(username);
    }

    // ===== DELETE TASK =====
    // Chỉ OWNER
    public boolean canDeleteTask(Long projectId, String username) {

        ProjectMember pm = projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("You are not a member of this project"));

        if (pm.getRole() != ProjectRole.OWNER) {
            throw new ResourceNotFoundException("Only OWNER can delete task");
        }
        return true;
    }
}
