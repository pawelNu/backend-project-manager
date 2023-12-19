package com.pawelnu.BackendProjectManager.controller;

import com.pawelnu.BackendProjectManager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringResponseDTO;
import com.pawelnu.BackendProjectManager.service.IProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
@Tag(name = "ProjectController")
public class ProjectController {

    private final IProjectService projectService;

    @GetMapping
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String field,
            @RequestParam(required = false) String direction) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(projectService.getAllProjects(pageNumber, pageSize, field, direction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.getProjectById(id));
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @RequestBody ProjectCreateRequestDTO projectCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(projectCreateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProjectById(@PathVariable UUID id) {
        String deletedProject = projectService.deleteProjectById(id);
        return ResponseEntity.status(HttpStatus.OK).body(deletedProject);
    }

    @PostMapping("/search")
    public ResponseEntity<ProjectFilteringResponseDTO> searchProject(
            @RequestBody(required = false) ProjectFilteringRequestDTO projectFilteringRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(projectService.searchProject(projectFilteringRequestDTO));
    }
}
