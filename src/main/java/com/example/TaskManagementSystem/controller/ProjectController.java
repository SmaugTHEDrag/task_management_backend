package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.project.ProjectDTO;
import com.example.TaskManagementSystem.dto.project.ProjectRequestDTO;
import com.example.TaskManagementSystem.service.project.IProjectService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Project API", description = "APIs for managing projects")
public class ProjectController {

    private final IProjectService projectService;

    @Operation(
            summary = "Get all projects",
            description = "Retrieve all projects in the system"
    )
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(){
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @Operation(
            summary = "Get project by ID",
            description = "Retrieve project details by project ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id){
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @Operation(
            summary = "Create new project",
            description = "Create a new project. The authenticated user becomes the project owner"
    )
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @RequestBody ProjectRequestDTO projectRequestDTO,
            Principal principal
    ){
        String username = principal.getName();
        return ResponseEntity.ok(projectService.createProject(projectRequestDTO, username));
    }

    @Operation(
            summary = "Get my projects",
            description = "Retrieve all projects owned by the authenticated user"
    )
    @GetMapping("/my-projects")
    public ResponseEntity<List<ProjectDTO>> getMyProjects(Principal principal) {
        return ResponseEntity.ok(projectService.getMyProjects(principal.getName()));
    }

    @Operation(
            summary = "Update project",
            description = "Update project information. Only project owner is allowed"
    )
    @PutMapping("/{id}")
    @PreAuthorize("@projectSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequestDTO projectRequestDTO
    ){
        return ResponseEntity.ok(projectService.updateProject(id, projectRequestDTO));
    }

    @Operation(
            summary = "Delete project",
            description = "Delete a project. Allowed for ADMIN or project owner"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @projectSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<String> deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project has been deleted");
    }
}

