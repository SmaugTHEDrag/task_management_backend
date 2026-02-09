package com.example.TaskManagementSystem.repository;

import com.example.TaskManagementSystem.entity.ProjectMember;
import com.example.TaskManagementSystem.entity.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    boolean existsByProjectIdAndUserId(Long projectId, Long userId);

    boolean existsByProject_IdAndUser_Username(Long projectId, String username);

    long countByProjectIdAndRole(Long projectId, ProjectRole role);

    Optional<ProjectMember> findByProject_IdAndUser_Username(Long projectId, String username);

    boolean existsByProject_IdAndUser_Id(Long projectId, Long userId);

    List<ProjectMember> findByProjectId(Long projectId);

    List<ProjectMember> findByUserId(Long userId);
}
