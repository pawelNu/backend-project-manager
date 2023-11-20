package com.pawelnu.BackendProjectManager.controller;

import com.pawelnu.BackendProjectManager.dto.ProjectDTO;
import com.pawelnu.BackendProjectManager.service.IProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {

    private final IProjectService projectService;

    @GetMapping("")
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(projectService.getAllProjects());
    }

}
