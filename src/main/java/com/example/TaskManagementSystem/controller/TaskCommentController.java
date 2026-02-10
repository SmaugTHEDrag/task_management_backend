package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.task.TaskCommentDTO;
import com.example.TaskManagementSystem.dto.task.TaskCommentRequestDTO;
import com.example.TaskManagementSystem.service.task.ITaskCommentService;
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
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/comments")
@RequiredArgsConstructor
@Tag(
        name = "6. Task Comments",
        description = "APIs for managing comments on tasks"
)
public class TaskCommentController {

    private final ITaskCommentService commentService;

    @Operation(
            summary = "Create a comment on a task",
            description = "Add a new comment to a task. Only authorized project members can comment."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project or task not found")
    })
    @PreAuthorize("@taskCommentSecurity.canComment(#projectId, #taskId, principal.username)")
    @PostMapping
    public ResponseEntity<TaskCommentDTO> create(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody TaskCommentRequestDTO dto,
            Principal principal) {

        return ResponseEntity.ok(
                commentService.create(projectId, taskId, dto, principal.getName())
        );
    }

    @Operation(
            summary = "Get all comments of a task",
            description = "Retrieve all comments belonging to a specific task"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping
    public ResponseEntity<List<TaskCommentDTO>> getAll(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                commentService.getByTask(taskId)
        );
    }

    @Operation(
            summary = "Update a comment",
            description = "Update a comment. Only the comment owner or authorized users can update it."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Comment or task not found")
    })
    @PreAuthorize("@taskCommentSecurity.canUpdate(#projectId, #taskId, #commentId, principal.username)")
    @PutMapping("/{commentId}")
    public ResponseEntity<TaskCommentDTO> update(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestBody TaskCommentRequestDTO dto) {

        return ResponseEntity.ok(
                commentService.update(projectId, taskId, commentId, dto)
        );
    }

    @Operation(
            summary = "Delete a comment",
            description = "Delete a comment from a task. Only the comment owner or authorized users can delete it."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Comment or task not found")
    })
    @PreAuthorize("@taskCommentSecurity.canDelete(#projectId, #taskId, #commentId, principal.username)")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> delete(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long commentId) {

        commentService.delete(projectId, taskId, commentId);
        return ResponseEntity.ok("Comment deleted");
    }
}
