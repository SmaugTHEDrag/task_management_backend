package com.example.TaskManagementSystem.mapper;

import com.example.TaskManagementSystem.dto.task.TaskAttachmentDTO;
import com.example.TaskManagementSystem.entity.TaskAttachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskAttachmentMapper {

    @Mapping(source = "uploadedBy.username", target = "uploadedBy")
    TaskAttachmentDTO toDTO(TaskAttachment attachment);
}

