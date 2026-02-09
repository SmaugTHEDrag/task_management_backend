package com.example.TaskManagementSystem.service.project;

import com.example.TaskManagementSystem.dto.project.AddMemberRequestDTO;
import com.example.TaskManagementSystem.dto.project.ProjectMemberDTO;
import com.example.TaskManagementSystem.entity.Project;
import com.example.TaskManagementSystem.entity.ProjectMember;
import com.example.TaskManagementSystem.entity.ProjectRole;
import com.example.TaskManagementSystem.entity.User;
import com.example.TaskManagementSystem.exception.ResourceNotFoundException;
import com.example.TaskManagementSystem.repository.IProjectMemberRepository;
import com.example.TaskManagementSystem.repository.IProjectRepository;
import com.example.TaskManagementSystem.repository.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProjectMemberService implements IProjectMemberService{
    private final IProjectRepository projectRepository;
    private final IProjectMemberRepository projectMemberRepository;
    private final IUserRepository userRepository;

    @Override
    public void addMember(Long projectId, AddMemberRequestDTO request) {

        // 1. check project tồn tại
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // 2. check user được add có tồn tại
        User userToAdd = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 3. không cho add trùng
        if (projectMemberRepository
                .existsByProject_IdAndUser_Id(projectId, userToAdd.getId())) {
            throw new IllegalStateException("User already in project");
        }

        // 4. create member
        ProjectMember member = new ProjectMember();
        member.setProject(project);
        member.setUser(userToAdd);
        member.setRole(ProjectRole.MEMBER);

        projectMemberRepository.save(member);
    }

    @Override
    public void removeMember(Long projectId, String username) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ProjectMember targetMember = projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found in project"));

        // Không cho xóa OWNER
        if (targetMember.getRole() == ProjectRole.OWNER) {
            throw new ResourceNotFoundException("Cannot remove project OWNER");
        }

        projectMemberRepository.delete(targetMember);
    }

    @Override
    public void leaveProject(Long projectId, String username) {
        ProjectMember pm = projectMemberRepository
                .findByProject_IdAndUser_Username(projectId, username)
                .orElseThrow(() -> new ResourceNotFoundException("Not a project member"));

        if (pm.getRole() == ProjectRole.OWNER) {
            throw new ResourceNotFoundException("OWNER cannot leave project");
        }

        projectMemberRepository.delete(pm);
    }

    @Override
    public List<ProjectMemberDTO> getMembers(Long projectId, String username) {

        // 1. check user có thuộc project không
        if (!projectMemberRepository
                .existsByProject_IdAndUser_Username(projectId, username)) {
            throw new ResourceNotFoundException("You are not a member of this project");
        }

        // 2. lấy danh sách members
        return projectMemberRepository.findByProjectId(projectId)
                .stream()
                .map(pm -> new ProjectMemberDTO(
                        pm.getUser().getId(),
                        pm.getUser().getUsername(),
                        pm.getRole().name(),
                        pm.getJoinedAt().toString()
                ))
                .toList();
    }

}
