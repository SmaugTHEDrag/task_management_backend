package com.example.TaskManagementSystem.service.task;

import com.example.TaskManagementSystem.dto.task.TaskCommentDTO;
import com.example.TaskManagementSystem.dto.task.TaskCommentRequestDTO;
import com.example.TaskManagementSystem.entity.Task;
import com.example.TaskManagementSystem.entity.TaskComment;
import com.example.TaskManagementSystem.entity.User;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.mapper.TaskCommentMapper;
import com.example.TaskManagementSystem.repository.ITaskCommentRepository;
import com.example.TaskManagementSystem.repository.ITaskRepository;

import com.example.TaskManagementSystem.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskCommentService implements ITaskCommentService {

    private final ITaskRepository taskRepository;
    private final IUserRepository userRepository;
    private final ITaskCommentRepository commentRepository;
    private final TaskCommentMapper commentMapper;

    @Override
    public TaskCommentDTO create(Long projectId, Long taskId, TaskCommentRequestDTO dto, String username) {
        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }

        Task task = taskRepository.findByIdAndProject_Id(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TaskComment comment = new TaskComment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setContent(dto.getContent());

        return commentMapper.toDTO(commentRepository.save(comment));
    }

    @Override
    public List<TaskCommentDTO> getByTask(Long taskId) {
        return commentRepository.findByTask_IdOrderByCreatedAtAsc(taskId)
                .stream()
                .map(commentMapper::toDTO)
                .toList();
    }

    @Override
    public TaskCommentDTO update(Long projectId, Long taskId, Long commentId, TaskCommentRequestDTO dto) {

        TaskComment comment = commentRepository
                .findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        if (dto.getContent() == null || dto.getContent().isBlank()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }

        comment.setContent(dto.getContent());
        return commentMapper.toDTO(comment);
    }

    @Override
    public void delete(Long projectId, Long taskId, Long commentId) {

        TaskComment comment = commentRepository
                .findByIdAndTask_Id(commentId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        commentRepository.delete(comment);
    }
}


