package com.pawelnu.backendprojectmanager.service;

import com.pawelnu.backendprojectmanager.dto.ProjectDto;
import com.pawelnu.backendprojectmanager.entity.ProjectEntity;
import com.pawelnu.backendprojectmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

}
