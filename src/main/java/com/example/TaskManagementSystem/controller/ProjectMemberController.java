package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.project.AddMemberRequestDTO;
import com.example.TaskManagementSystem.dto.project.ProjectMemberDTO;
import com.example.TaskManagementSystem.service.project.IProjectMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "4. Project Members", description = "APIs for managing project members (view, add, remove, leave)")
public class ProjectMemberController {

    private final IProjectMemberService projectMemberService;

    @Operation(summary = "Get project members", description = "Get all members of a project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project members retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberDTO>> getProjectMembers(@PathVariable Long projectId, Principal principal)
    {
        return ResponseEntity.ok(projectMemberService.getMembers(projectId, principal.getName()));
    }

    @Operation(summary = "Add member to project", description = "Add a new member to the project (Only Project Owner)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member added successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project or user not found")
    })
    @PostMapping("/{projectId}/members")
    @PreAuthorize("@projectSecurity.isOwner(#projectId, authentication.name)")
    public ResponseEntity<String> addMember(@PathVariable Long projectId, @RequestBody AddMemberRequestDTO request)
    {
        projectMemberService.addMember(projectId, request);
        return ResponseEntity.ok("Member " + request.getUsername() + " added successfully");
    }

    @Operation(summary = "Remove member from project", description = "Remove member from the project (Only Project Owner)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member removed successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project or member not found")
    })
    @DeleteMapping("/{projectId}/members/{username}")
    @PreAuthorize("@projectSecurity.isOwner(#projectId, authentication.name)")
    public ResponseEntity<String> removeMember(@PathVariable Long projectId, @PathVariable String username)
    {
        projectMemberService.removeMember(projectId, username);
        return ResponseEntity.ok("Member " + username + " removed successfully");
    }


    @Operation(summary = "Leave project", description = "User leaves the project voluntarily.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Left project successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @DeleteMapping("/{projectId}/members/leave")
    public ResponseEntity<String> leaveProject(@PathVariable Long projectId, Principal principal)
    {
        projectMemberService.leaveProject(projectId, principal.getName());
        return ResponseEntity.ok("You have left the project");
    }
}
