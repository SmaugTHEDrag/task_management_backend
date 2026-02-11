package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.task.TaskDTO;
import com.example.TaskManagementSystem.dto.task.TaskRequestDTO;
import com.example.TaskManagementSystem.service.task.ITaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
@RequiredArgsConstructor
@Tag(name = "5. Tasks", description = "APIs for managing tasks inside a project")
public class TaskController {

    private final ITaskService taskService;

    @Operation(summary = "Create a new task", description = "Create a task inside a specific project (Only Project Member)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PreAuthorize("@taskSecurity.canCreateTask(#projectId, principal.username)")
    @PostMapping
    public ResponseEntity<TaskDTO> create(@PathVariable Long projectId, @RequestBody TaskRequestDTO dto, Principal principal)
    {
        return ResponseEntity.ok(taskService.createTask(projectId, dto, principal.getName()));
    }


    @Operation(summary = "Get all tasks by project", description = "Get all tasks belonging to a specific project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAll(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }


    @Operation(summary = "Get task detail", description = "Get detailed information of a task in a project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task detail retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Task or project not found")
    })
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> detail(@PathVariable Long projectId, @PathVariable Long taskId)
    {
        return ResponseEntity.ok(taskService.getTaskDetail(projectId, taskId));
    }


    @Operation(summary = "Update a task", description = "Update task information (Only Project Owner or Task Creator")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task or project not found")
    })
    @PreAuthorize("@taskSecurity.canUpdateTask(#projectId, #taskId, principal.username)")
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> update(@PathVariable Long projectId, @PathVariable Long taskId,
                                          @RequestBody TaskRequestDTO dto, Principal principal)
    {
        return ResponseEntity.ok(taskService.updateTask(projectId, taskId, dto, principal.getName()));
    }


    @Operation(summary = "Delete a task", description = "Delete a task from a project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Task or project not found")
    })
    @PreAuthorize("@taskSecurity.canDeleteTask(#projectId, principal.username)")
    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> delete(@PathVariable Long projectId, @PathVariable Long taskId, Principal principal) {

        taskService.deleteTask(projectId, taskId, principal.getName());
        return ResponseEntity.ok("Task deleted");
    }
}
