package com.example.TaskManagementSystem.repository;

import com.example.TaskManagementSystem.entity.TaskAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITaskAttachmentRepository extends JpaRepository<TaskAttachment, Long> {
    List<TaskAttachment> findByComment_Id(Long commentId);
}
