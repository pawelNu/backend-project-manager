package com.pawelnu.backendprojectmanager.service;

import com.pawelnu.backendprojectmanager.dto.ProjectDto;
import com.pawelnu.backendprojectmanager.entity.ProjectEntity;
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

    public List<ProjectEntity> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<ProjectEntity> getAllProjectsWithSorting(String field, String direction) {
        if (direction.equalsIgnoreCase("asc")) {
            return projectRepository.findAll(Sort.by(Sort.Direction.ASC, field));
        } else if (direction.equalsIgnoreCase("desc")) {
            return projectRepository.findAll(Sort.by(Sort.Direction.DESC, field));
        } else {
            return projectRepository.findAll();
        }
    }

    public Page<ProjectEntity> getAllProjectsWithPagination(int pageNumber, int pageSize) {
        return projectRepository.findAll(PageRequest.of(pageNumber, pageSize));
    }

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

    // TODO add page number validation, it should be impossible to choose page number higher than total number of pages
    // TODO check if it is possible to return something similar to response from data rest

    public Optional<ProjectEntity> getProjectById(UUID id) {
        return projectRepository.findById(id);
    }

    public String addProject(ProjectDto projectDto) {
        ProjectEntity newProject = new ProjectEntity();
        newProject.setName(projectDto.getName());
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
        projectRepository.save(projectToEdit);
        return "Project " + projectDto.getName() + " was updated.";
    }

    public List<ProjectEntity> searchProjectByName(String searchTerm) {
        return projectRepository.findByNameContainingIgnoreCase(searchTerm);
    }

    public Boolean isProjectNameAlreadyExist(String name) {
        return projectRepository.existsByName(name);
    }

}
