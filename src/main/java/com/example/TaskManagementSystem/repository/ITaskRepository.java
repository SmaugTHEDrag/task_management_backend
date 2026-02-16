package com.example.TaskManagementSystem.repository;

import com.example.TaskManagementSystem.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ITaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject_Id(Long projectId);

    @Query("SELECT t FROM Task t " +
           "LEFT JOIN FETCH t.assignee " +
           "LEFT JOIN FETCH t.createdBy " +
           "WHERE t.id = :taskId AND t.project.id = :projectId")
    Optional<Task> findByIdAndProject_Id(@Param("taskId") Long taskId, @Param("projectId") Long projectId);
    
    // Query to get assignee username
    @Query("SELECT u.username FROM Task t " +
           "JOIN t.assignee u " +
           "WHERE t.id = :taskId AND t.project.id = :projectId")
    Optional<String> findAssigneeUsername(@Param("taskId") Long taskId, @Param("projectId") Long projectId);
}
