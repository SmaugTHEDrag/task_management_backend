package com.example.TaskManagementSystem.service.project;

import com.example.TaskManagementSystem.dto.project.AddMemberRequestDTO;
import com.example.TaskManagementSystem.dto.project.ProjectMemberDTO;

import java.util.List;

public interface IProjectMemberService {
    void addMember(Long projectId, AddMemberRequestDTO addMemberRequestDTO);
    void removeMember(Long projectId, String username);
    void leaveProject(Long projectId, String username);
    List<ProjectMemberDTO> getMembers(Long projectId, String username);
}
