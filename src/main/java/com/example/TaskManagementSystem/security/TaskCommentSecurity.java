package com.example.TaskManagementSystem.security;

import com.example.TaskManagementSystem.entity.ProjectMember;
import com.example.TaskManagementSystem.entity.ProjectRole;
import com.example.TaskManagementSystem.entity.TaskComment;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.repository.IProjectMemberRepository;
import com.example.TaskManagementSystem.repository.ITaskCommentRepository;
import com.example.TaskManagementSystem.repository.ITaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("taskCommentSecurity")
@RequiredArgsConstructor
public class TaskCommentSecurity {

    private final IProjectMemberRepository projectMemberRepository;
    private final ITaskRepository taskRepository;
    private final ITaskCommentRepository commentRepository;

    // ===== CREATE =====
    public boolean canComment(Long projectId, Long taskId, String username) {

        projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("You are not a member of this project"));

        taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Task not found"));

        return true;
    }

    // ===== UPDATE =====
    public boolean canUpdate(Long projectId, Long taskId, Long commentId, String username) {

        TaskComment comment = commentRepository
                .findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment not found"));

        return comment.getUser().getUsername().equals(username);
    }

    // ===== DELETE =====
    public boolean canDelete(Long projectId, Long taskId, Long commentId, String username) {

        TaskComment comment = commentRepository
                .findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Comment not found"));

        // owner comment
        if (comment.getUser().getUsername().equals(username)) {
            return true;
        }

        // project OWNER
        ProjectMember pm = projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Not project member"));

        return pm.getRole() == ProjectRole.OWNER;
    }
}

