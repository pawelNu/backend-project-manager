package com.pawelnu.BackendProjectManager.service;

import com.pawelnu.BackendProjectManager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectDTO;
import java.util.UUID;

import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringRequestDTO;
import org.springframework.data.domain.Page;

public interface IProjectService {

    Page<ProjectDTO> getAllProjects(
            Integer pageNumber, Integer pageSize, String field, String direction);

    ProjectDTO getProjectById(UUID id);

    ProjectDTO createProject(ProjectCreateRequestDTO projectCreateRequest);

    String deleteProjectById(UUID id);

    Page<ProjectDTO> searchProject(ProjectFilteringRequestDTO projectFilteringRequestDTO);
}
