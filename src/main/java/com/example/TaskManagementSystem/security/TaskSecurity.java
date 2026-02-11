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


    // MEMBER trong project đều được tạo task
    public boolean canCreateTask(Long projectId, String username) {
        projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("You are not a member of this project"));
        return true;
    }


    // OWNER hoặc TASK CREATOR hoặc ASSIGNEE
    public boolean canUpdateTask(Long projectId, Long taskId, String username) {

        ProjectMember pm = projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("You are not a member of this project"));

        Task task = taskRepository
                .findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        // OWNER luôn được
        if (pm.getRole() == ProjectRole.OWNER) {
            return true;
        }

        // TASK CREATOR được update
        if (task.getCreatedBy() != null &&
                task.getCreatedBy().getUsername().equals(username)) {
            return true;
        }

        // ASSIGNEE được update
        return task.getAssignee() != null &&
                task.getAssignee().getUsername().equals(username);
    }


    // OWNER hoặc TASK CREATOR
    public boolean canDeleteTask(Long projectId, Long taskId, String username) {

        ProjectMember pm = projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("You are not a member of this project"));

        Task task = taskRepository
                .findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        // OWNER được xoá
        if (pm.getRole() == ProjectRole.OWNER) {
            return true;
        }

        // TASK CREATOR được xoá
        if (task.getCreatedBy() != null && task.getCreatedBy().getUsername().equals(username)) {
            return true;
        }

        throw new ResourceNotFoundException("You don't have permission to delete this task");
    }
}
