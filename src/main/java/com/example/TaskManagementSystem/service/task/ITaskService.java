package com.example.TaskManagementSystem.service.task;

import com.example.TaskManagementSystem.dto.task.TaskDTO;
import com.example.TaskManagementSystem.dto.task.TaskRequestDTO;
import com.example.TaskManagementSystem.entity.Task;

import java.util.List;

public interface ITaskService {
    TaskDTO createTask(Long projectId, TaskRequestDTO dto, String username);
    List<TaskDTO> getTasksByProject(Long projectId);
    TaskDTO getTaskDetail(Long projectId, Long taskId);
    TaskDTO updateTask(Long projectId, Long taskId, TaskRequestDTO dto, String username);
    void deleteTask(Long projectId, Long taskId, String username);
}
