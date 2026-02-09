package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.task.TaskDTO;
import com.example.TaskManagementSystem.dto.task.TaskRequestDTO;
import com.example.TaskManagementSystem.service.task.ITaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final ITaskService taskService;

    @PreAuthorize("@taskSecurity.canCreateTask(#projectId, principal.username)")
    @PostMapping
    public ResponseEntity<TaskDTO> create(@PathVariable Long projectId, @RequestBody TaskRequestDTO dto, Principal principal) {
        return ResponseEntity.ok(taskService.createTask(projectId, dto, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAll(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> detail(@PathVariable Long projectId, @PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskDetail(projectId, taskId));
    }

    @PreAuthorize("@taskSecurity.canUpdateTask(#projectId, #taskId, principal.username)")
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long projectId, @PathVariable Long taskId,
                                          @RequestBody TaskRequestDTO dto, Principal principal) {

        return ResponseEntity.ok(taskService.updateTask(projectId, taskId, dto, principal.getName()));
    }

    @PreAuthorize("@taskSecurity.canDeleteTask(#projectId, principal.username)")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Long projectId, @PathVariable Long taskId, Principal principal) {
        taskService.deleteTask(projectId, taskId, principal.getName());
        return ResponseEntity.ok("Task deleted");
    }
}


