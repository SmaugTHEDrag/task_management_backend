package com.example.TaskManagementSystem.repository;

import com.example.TaskManagementSystem.entity.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ITaskCommentRepository extends JpaRepository<TaskComment, Long> {

    List<TaskComment> findByTask_IdOrderByCreatedAtAsc(Long taskId);

    Optional<TaskComment> findByIdAndTask_Id(Long id, Long taskId);
}

