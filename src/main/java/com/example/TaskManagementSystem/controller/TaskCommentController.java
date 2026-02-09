package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.task.TaskCommentDTO;
import com.example.TaskManagementSystem.dto.task.TaskCommentRequestDTO;
import com.example.TaskManagementSystem.service.task.ITaskCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class TaskCommentController {

    private final ITaskCommentService commentService;

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

    @GetMapping
    public ResponseEntity<List<TaskCommentDTO>> getAll(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getByTask(taskId));
    }

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
