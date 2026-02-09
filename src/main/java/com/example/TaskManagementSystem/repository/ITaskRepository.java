package com.example.TaskManagementSystem.repository;

import com.example.TaskManagementSystem.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ITaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject_Id(Long projectId);

    Optional<Task> findByIdAndProject_Id(Long taskId, Long projectId);
}
