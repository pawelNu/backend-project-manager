package com.pawelnu.projectmanager.service;

import com.pawelnu.projectmanager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.projectmanager.dto.project.ProjectDTO;
import com.pawelnu.projectmanager.dto.project.ProjectFilteringRequestDTO;
import com.pawelnu.projectmanager.dto.project.ProjectFilteringResponseDTO;
import java.util.UUID;

public interface IProjectService {

  ProjectFilteringResponseDTO getAllProjects(
      Integer pageNumber, Integer pageSize, String field, Boolean isAscendingSorting);

  ProjectDTO getProjectById(UUID id);

  ProjectDTO createProject(ProjectCreateRequestDTO projectCreateRequest);

  String deleteProjectById(UUID id);

  ProjectFilteringResponseDTO searchProject(ProjectFilteringRequestDTO projectFilteringRequestDTO);
}
