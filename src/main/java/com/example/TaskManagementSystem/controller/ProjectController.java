package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.dto.project.ProjectDTO;
import com.example.TaskManagementSystem.dto.project.ProjectRequestDTO;
import com.example.TaskManagementSystem.service.project.IProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
@Tag(name = "3. Projects", description = "APIs for managing projects")
public class ProjectController {

    private final IProjectService projectService;

    @Operation(summary = "Get all projects", description = "Get all projects in the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(){
        return ResponseEntity.ok(projectService.getAllProjects());
    }


    @Operation(summary = "Get project by ID", description = "Get project details by project ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id)
    {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }


    @Operation(summary = "Create new project", description = "Create a new project (User created become Project Owner)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectRequestDTO projectRequestDTO, Principal principal)
    {
        return ResponseEntity.ok(projectService.createProject(projectRequestDTO, principal.getName()));
    }


    @Operation(summary = "Get my projects", description = "Get all projects owned by user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User projects retrieved successfully")
    })
    @GetMapping("/my-projects")
    public ResponseEntity<List<ProjectDTO>> getMyProjects(Principal principal)
    {
        return ResponseEntity.ok(projectService.getMyProjects(principal.getName()));
    }


    @Operation(summary = "Update project", description = "Update project information (Only Project Owner)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("@projectSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDTO projectRequestDTO)
    {
        return ResponseEntity.ok(projectService.updateProject(id, projectRequestDTO));
    }


    @Operation(summary = "Delete project", description = "Delete a project (Only ADMIN or Project Owner)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or @projectSecurity.isOwner(#id, authentication.name)")
    public ResponseEntity<String> deleteProject(@PathVariable Long id)
    {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project has been deleted");
    }
}
