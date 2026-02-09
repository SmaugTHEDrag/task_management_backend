package com.example.TaskManagementSystem.security;

import com.example.TaskManagementSystem.entity.ProjectMember;
import com.example.TaskManagementSystem.entity.ProjectRole;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.repository.IProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("projectSecurity")
@RequiredArgsConstructor
public class ProjectSecurity {

    private final IProjectMemberRepository projectMemberRepository;

    // check if the user is OWNER
    public boolean isOwner(Long projectId, String username){
        ProjectMember pm = projectMemberRepository.findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(()-> new ResourceNotFoundException("You are not a member of this project"));

        if (pm.getRole() != ProjectRole.OWNER) {
            throw new ResourceNotFoundException("Only OWNER can perform this action");
        }
        return true;
    }
}
