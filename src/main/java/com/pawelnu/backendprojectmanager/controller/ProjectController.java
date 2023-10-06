package com.pawelnu.backendprojectmanager.controller;

import com.pawelnu.backendprojectmanager.dto.ProjectDto;
import com.pawelnu.backendprojectmanager.entity.ProjectEntity;
import com.pawelnu.backendprojectmanager.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projects")
@Tag(name = "ProjectController")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("")
    public List<ProjectEntity> getAllProjects() {
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public Optional<ProjectEntity> getProjectById(@PathVariable("id") UUID id) {
        return projectService.getProjectById(id);
    }

    @PostMapping("")
    public ResponseEntity<String> addProject(@Valid @RequestBody ProjectDto projectDto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.join(", ", errors));
        }

        if (projectService.isProjectNameAlreadyExist(projectDto.getName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Project name already exists!");
        }

        String result = projectService.addProject(projectDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable("id") UUID id) {
        String result = projectService.deleteProject(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProject(@PathVariable("id") UUID id,
                                                @Valid @RequestBody ProjectDto projectDto,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.join(", ", errors));
        }

        String result = projectService.updateProject(id, projectDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/search-by-name")
    public List<ProjectEntity> searchProjectByName(@RequestParam String searchTerm) {
        return projectService.searchProjectByName(searchTerm);
    }
}
