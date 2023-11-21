package com.pawelnu.BackendProjectManager.service.impl;

import com.pawelnu.BackendProjectManager.dto.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.ProjectDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.exception.NotFoundException;
import com.pawelnu.BackendProjectManager.mapper.ProjectMapper;
import com.pawelnu.BackendProjectManager.repository.ProjectRepository;
import com.pawelnu.BackendProjectManager.service.IProjectService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements IProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        return projectEntities.stream().map(projectMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getProjectById(UUID projectId) {
        Optional<ProjectEntity> projectById = projectRepository.findById(projectId);
        if (projectById.isPresent()) {
            return projectMapper.toDTO(projectById.get());
        } else {
            throw new NotFoundException("Project not found with id: " + projectId);
        }
    }

    @Override
    public ProjectDTO createProject(ProjectCreateRequestDTO projectCreateRequest) {
        ProjectEntity projectEntity = projectMapper.toEntity(projectCreateRequest);
        ProjectEntity savedProject = projectRepository.save(projectEntity);
        return projectMapper.toDTO(savedProject);
    }
}
