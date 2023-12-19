package com.pawelnu.BackendProjectManager.service;

import com.pawelnu.BackendProjectManager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringResponseDTO;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface IProjectService {

    Page<ProjectDTO> getAllProjects(
            Integer pageNumber, Integer pageSize, String field, String direction);

    ProjectDTO getProjectById(UUID id);

    ProjectDTO createProject(ProjectCreateRequestDTO projectCreateRequest);

    String deleteProjectById(UUID id);

    ProjectFilteringResponseDTO searchProject(
            ProjectFilteringRequestDTO projectFilteringRequestDTO);
}
