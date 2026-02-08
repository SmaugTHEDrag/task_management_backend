package com.example.TaskManagementSystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /* ================= Relations ================= */
    @OneToMany(mappedBy = "user")
    private Set<ProjectMember> members;

    @OneToMany(mappedBy = "createdBy")
    private Set<Project> project;

    @OneToMany(mappedBy = "assignee")
    private List<Task> tasks;

    @OneToMany(mappedBy = "user")
    private List<TaskComment> comments;

    @OneToMany(mappedBy = "uploadedBy")
    private List<TaskAttachment> attachments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TaskActivityLog> activityLog;
}
