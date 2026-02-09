package com.example.TaskManagementSystem.dto.task;

import com.example.TaskManagementSystem.entity.TaskPriority;
import com.example.TaskManagementSystem.entity.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String  priority;
    private String dueDate;

    private String assignee;   // username
    private String createdAt;
    private String updatedAt;
}
