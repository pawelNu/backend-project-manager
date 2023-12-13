package com.pawelnu.BackendProjectManager.service.impl;

import com.pawelnu.BackendProjectManager.dto.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.ProjectDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.exception.NotFoundException;
import com.pawelnu.BackendProjectManager.mapper.ProjectMapper;
import com.pawelnu.BackendProjectManager.repository.ProjectRepository;
import com.pawelnu.BackendProjectManager.service.IProjectService;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements IProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    @Override
    public Page<ProjectDTO> getAllProjects(
            Integer pageNumber, Integer pageSize, String field, String direction) {

        Sort sort = Sort.unsorted();

        if (direction == null || direction.isEmpty()) {
            sort = Sort.unsorted();
        } else if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(Sort.Direction.ASC, field);
        } else if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, field);
        }

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = 0;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = 25;
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        return projectRepository.findAll(pageRequest).map(projectMapper::toDTO);
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
