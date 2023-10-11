package com.pawelnu.backendprojectmanager.service;

import com.pawelnu.backendprojectmanager.dto.ProjectDto;
import com.pawelnu.backendprojectmanager.entity.ProjectEntity;
import com.pawelnu.backendprojectmanager.enumeration.DefinitionState;
import com.pawelnu.backendprojectmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public Page<ProjectEntity> getAllProjectsWithSortingAndPagination(String field, String direction,
                                                                      Integer pageNumber, Integer pageSize) {
        Sort sort = Sort.unsorted();
        if (direction == null || direction.isEmpty()) {
            sort = Sort.unsorted();
        } else if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(Sort.Direction.ASC, field);
        } else if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, field);
        }

        if (pageNumber == null) {
            pageNumber = 0;
        }

        if (pageSize == null) {
            pageSize = 25;
        }

        return projectRepository.findAll(PageRequest.of(pageNumber, pageSize, sort));
    }

    public Optional<ProjectEntity> getProjectById(UUID id) {
        return projectRepository.findById(id);
    }

    public String addProject(ProjectDto projectDto) {
        ProjectEntity newProject = new ProjectEntity();

        newProject.setName(projectDto.getName());
        newProject.setFinished(DefinitionState.NO);

        projectRepository.save(newProject);
        return "Project " + projectDto.getName() + " was added.";
    }

    public String deleteProject(UUID id) {
        projectRepository.deleteById(id);
        return "Project was deleted.";
    }

    public String updateProject(UUID id, ProjectDto projectDto) {
        ProjectEntity projectToEdit = projectRepository.getReferenceById(id);

        projectToEdit.setName(projectDto.getName());
        projectToEdit.setFinished(projectDto.getFinished());

        projectRepository.save(projectToEdit);
        return "Project " + projectDto.getName() + " was updated.";
    }

    public List<ProjectEntity> searchProjectByName(String name) {
        return projectRepository.findByNameContainingIgnoreCase(name);
    }

    public Boolean isProjectNameAlreadyExists(String name) {
        return projectRepository.existsByName(name);
    }

}
