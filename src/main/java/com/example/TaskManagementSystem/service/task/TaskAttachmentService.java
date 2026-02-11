package com.example.TaskManagementSystem.service.task;

import com.example.TaskManagementSystem.dto.task.TaskAttachmentDTO;
import com.example.TaskManagementSystem.entity.TaskAttachment;
import com.example.TaskManagementSystem.entity.TaskComment;
import com.example.TaskManagementSystem.entity.User;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.file.FileUploadService;
import com.example.TaskManagementSystem.mapper.TaskAttachmentMapper;
import com.example.TaskManagementSystem.repository.ITaskAttachmentRepository;
import com.example.TaskManagementSystem.repository.ITaskCommentRepository;
import com.example.TaskManagementSystem.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskAttachmentService implements ITaskAttachmentService {

    private final ITaskAttachmentRepository attachmentRepository;
    private final ITaskCommentRepository commentRepository;
    private final IUserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final TaskAttachmentMapper attachmentMapper;

    @Override
    public TaskAttachmentDTO upload(Long projectId, Long taskId, Long commentId, MultipartFile file, String username)
    {
        TaskComment comment = commentRepository
                .findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        try {
            String fileUrl = fileUploadService.uploadFile(file, "task-comments/" + commentId);

            TaskAttachment attachment = new TaskAttachment();
            attachment.setComment(comment);
            attachment.setUploadedBy(user);
            attachment.setFileName(file.getOriginalFilename());
            attachment.setFileUrl(fileUrl);

            return attachmentMapper.toDTO(attachmentRepository.save(attachment));

        } catch (Exception e) {
            throw new RuntimeException("Upload file failed", e);
        }
    }

    @Override
    public List<TaskAttachmentDTO> getByComment(Long commentId) {
        return attachmentRepository.findByComment_Id(commentId)
                .stream()
                .map(attachmentMapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long attachmentId, String username) {

        TaskAttachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));

        // chỉ uploader được xoá
        if (!attachment.getUploadedBy().getUsername().equals(username)) {
            throw new RuntimeException("You are not allowed to delete this attachment");
        }

        attachmentRepository.delete(attachment);
    }
}

