package com.example.TaskManagementSystem.repository;

import com.example.TaskManagementSystem.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT DISTINCT p\n" +
            "    FROM Project p\n" +
            "    JOIN ProjectMember pm ON pm.project = p\n" +
            "    JOIN pm.user u\n" +
            "    WHERE u.username = :username")
    List<Project> findAllProjectsByUsername(@Param("username")String username);
}

