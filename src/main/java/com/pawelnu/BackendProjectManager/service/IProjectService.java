package com.pawelnu.BackendProjectManager.service;

import com.pawelnu.BackendProjectManager.dto.ProjectDTO;

import java.util.List;

public interface IProjectService {

    List<ProjectDTO> getAllProjects();

}
