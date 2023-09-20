package com.pawelnu.backendprojectmanager.controller;

import com.pawelnu.backendprojectmanager.dto.ProjectDto;
import com.pawelnu.backendprojectmanager.entity.ProjectEntity;
import com.pawelnu.backendprojectmanager.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public String addProject(@RequestBody ProjectDto projectDto) {
        return projectService.addProject(projectDto);
    }

    @DeleteMapping("/{id}")
    public String deleteProject(@PathVariable("id") UUID id) {
        return projectService.deleteProject(id);
    }

    @PutMapping("/{id}")
    public String updateProject(@PathVariable("id") UUID id,
                                @RequestBody ProjectDto projectDto) {
        return projectService.updateProject(id, projectDto);
    }
}
