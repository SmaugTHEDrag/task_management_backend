package com.example.TaskManagementSystem.service.task;

import com.example.TaskManagementSystem.dto.task.TaskCommentDTO;
import com.example.TaskManagementSystem.dto.task.TaskCommentRequestDTO;

import java.util.List;

public interface ITaskCommentService {
    TaskCommentDTO create(Long projectId, Long taskId, TaskCommentRequestDTO dto, String username);
    List<TaskCommentDTO> getByTask(Long taskId);
    TaskCommentDTO update(Long projectId, Long taskId, Long commentId, TaskCommentRequestDTO dto);
    void delete(Long projectId, Long taskId, Long commentId);
}

