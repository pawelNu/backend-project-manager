package com.pawelnu.BackendProjectManager.service;

import com.pawelnu.BackendProjectManager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringResponseDTO;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface IProjectService {

    Page<ProjectDTO> getAllProjects(
            Integer pageNumber, Integer pageSize, String field, Boolean direction);

    ProjectDTO getProjectById(UUID id);

    ProjectDTO createProject(ProjectCreateRequestDTO projectCreateRequest);

    String deleteProjectById(UUID id);

    ProjectFilteringResponseDTO searchProject(
            ProjectFilteringRequestDTO projectFilteringRequestDTO);
}
