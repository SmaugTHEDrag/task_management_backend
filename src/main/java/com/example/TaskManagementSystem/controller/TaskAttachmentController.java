package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.task.TaskAttachmentDTO;
import com.example.TaskManagementSystem.service.task.ITaskAttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/tasks/{taskId}/comments/{commentId}/attachments")
public class TaskAttachmentController {

    private final ITaskAttachmentService attachmentService;

    // ===== Upload attachment =====
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @PreAuthorize("@taskSecurity.canUpdateTask(#projectId, #taskId, principal.username)")
    public ResponseEntity<TaskAttachmentDTO> upload(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @PathVariable Long commentId,
            @RequestParam("file") MultipartFile file,
            Principal principal
    ) {
        return ResponseEntity.ok(
                attachmentService.upload(
                        projectId,
                        taskId,
                        commentId,
                        file,
                        principal.getName()
                )
        );
    }

    // ===== Get attachments of a comment =====
    @GetMapping
    public ResponseEntity<List<TaskAttachmentDTO>> getByComment(
            @PathVariable Long commentId
    ) {
        return ResponseEntity.ok(
                attachmentService.getByComment(commentId)
        );
    }

    // ===== Delete attachment =====
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<String> delete(
            @PathVariable Long attachmentId,
            Principal principal
    ) {
        attachmentService.delete(attachmentId, principal.getName());
        return ResponseEntity.ok("Attachment deleted");
    }
}
