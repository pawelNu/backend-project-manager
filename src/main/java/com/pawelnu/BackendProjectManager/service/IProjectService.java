package com.pawelnu.BackendProjectManager.service;

import com.pawelnu.BackendProjectManager.dto.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.ProjectDTO;
import java.util.UUID;
import org.springframework.data.domain.Page;

public interface IProjectService {

    Page<ProjectDTO> getAllProjects(
            Integer pageNumber, Integer pageSize, String field, String direction);

    ProjectDTO getProjectById(UUID projectId);

    ProjectDTO createProject(ProjectCreateRequestDTO projectCreateRequest);
}
