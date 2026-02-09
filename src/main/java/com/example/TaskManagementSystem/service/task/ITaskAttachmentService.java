package com.example.TaskManagementSystem.service.task;

import com.example.TaskManagementSystem.dto.task.TaskAttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ITaskAttachmentService {
    TaskAttachmentDTO upload(
            Long projectId,
            Long taskId,
            Long commentId,
            MultipartFile file,
            String username
    );

    List<TaskAttachmentDTO> getByComment(Long commentId);

    void delete(Long attachmentId, String username);
}
