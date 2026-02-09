package com.example.TaskManagementSystem.dto.task;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskAttachmentDTO {
    private Long id;
    private String fileName;
    private String fileUrl;
    private String uploadedBy;
    private LocalDateTime createdAt;
}

