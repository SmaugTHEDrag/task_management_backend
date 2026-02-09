package com.example.TaskManagementSystem.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCommentDTO {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
}

