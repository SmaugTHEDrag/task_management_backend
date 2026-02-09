package com.example.TaskManagementSystem.dto.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberDTO {
    private Long userId;
    private String username;
    private String role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String joinedAt;
}

