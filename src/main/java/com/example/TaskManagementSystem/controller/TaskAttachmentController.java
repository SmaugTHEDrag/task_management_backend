package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.task.TaskAttachmentDTO;
import com.example.TaskManagementSystem.service.task.ITaskAttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "7. Task Attachments", description = "APIs for managing file attachments in task comments")
public class TaskAttachmentController {

    private final ITaskAttachmentService attachmentService;

    @Operation(summary = "Upload attachment to a comment", description = "Upload a file attachment to a task comment. Only authorized users can upload attachments.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attachment uploaded successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project, task, or comment not found")
    })
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    @PreAuthorize("@taskSecurity.canUpdateTask(#projectId, #taskId, principal.username)")
    public ResponseEntity<TaskAttachmentDTO> upload(@PathVariable Long projectId, @PathVariable Long taskId, @PathVariable Long commentId, @RequestParam("file") MultipartFile file, Principal principal)
    {
        return ResponseEntity.ok(attachmentService.upload(projectId, taskId, commentId, file, principal.getName()));
    }


    @Operation(summary = "Get attachments by comment", description = "Get all file attachments associated with a specific comment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attachments retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @GetMapping
    public ResponseEntity<List<TaskAttachmentDTO>> getByComment(@PathVariable Long commentId)
    {
        return ResponseEntity.ok(attachmentService.getByComment(commentId));
    }


    @Operation(summary = "Delete an attachment", description = "Delete a file attachment (Only uploader)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attachment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Attachment not found")
    })
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<String> delete(@PathVariable Long attachmentId, Principal principal)
    {
        attachmentService.delete(attachmentId, principal.getName());
        return ResponseEntity.ok("Attachment deleted");
    }
}
