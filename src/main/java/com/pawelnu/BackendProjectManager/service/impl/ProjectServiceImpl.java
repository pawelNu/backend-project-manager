package com.pawelnu.BackendProjectManager.service.impl;

import com.pawelnu.BackendProjectManager.dto.ProjectDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.mapper.ProjectMapper;
import com.pawelnu.BackendProjectManager.repository.ProjectRepository;
import com.pawelnu.BackendProjectManager.service.IProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements IProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;

    @Override
    public List<ProjectDTO> getAllProjects() {
        List<ProjectEntity> projectEntities = projectRepository.findAll();
        return projectEntities.stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
    }

}
