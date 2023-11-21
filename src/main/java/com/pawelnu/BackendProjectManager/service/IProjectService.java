package com.pawelnu.BackendProjectManager.service;

import com.pawelnu.BackendProjectManager.dto.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.ProjectDTO;
import java.util.List;
import java.util.UUID;

public interface IProjectService {

    List<ProjectDTO> getAllProjects();

    ProjectDTO getProjectById(UUID projectId);

    ProjectDTO createProject(ProjectCreateRequestDTO projectCreateRequest);
}
