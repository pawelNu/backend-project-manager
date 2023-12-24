package com.pawelnu.BackendProjectManager.service.impl;

import com.pawelnu.BackendProjectManager.dto.project.ProjectCreateRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringRequestDTO;
import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringResponseDTO;
import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
import com.pawelnu.BackendProjectManager.enums.Messages;
import com.pawelnu.BackendProjectManager.enums.PageValues;
import com.pawelnu.BackendProjectManager.exception.NotFoundException;
import com.pawelnu.BackendProjectManager.exception.NotNullOrEmptyException;
import com.pawelnu.BackendProjectManager.mapper.PagingAndSortingMapper;
import com.pawelnu.BackendProjectManager.mapper.ProjectMapper;
import com.pawelnu.BackendProjectManager.repository.project.ProjectRepository;
import com.pawelnu.BackendProjectManager.repository.project.ProjectSpecification;
import com.pawelnu.BackendProjectManager.service.IProjectService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements IProjectService {

    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final PagingAndSortingMapper pagingAndSortingMapper;

    @Override
    public Page<ProjectDTO> getAllProjects(
            Integer pageNumber, Integer pageSize, String field, String direction) {
        // TODO change String direction to Boolean direction
        //  on the pattern com.pawelnu.BackendProjectManager.mapper.PagingAndSortingMapper.toPageable

        if (field == null || field.isEmpty()) {
            throw new NotNullOrEmptyException(Messages.PROJECT_SORTING_FIELD_CANNOT_BE_NULL_OR_EMPTY.getMsg());
        }

        Sort sort = Sort.unsorted();

        if (direction == null || direction.isEmpty()) {
            sort = Sort.unsorted();
        } else if (direction.equalsIgnoreCase("asc")) {
            sort = Sort.by(Sort.Direction.ASC, field);
        } else if (direction.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, field);
        }

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = PageValues.PAGE_NUMBER.getValue();
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = PageValues.PAGE_SIZE.getValue();
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        // TODO customize Response like
        //  com.pawelnu.BackendProjectManager.service.impl.ProjectServiceImpl.searchProject

        return projectRepository.findAll(pageRequest).map(projectMapper::toDTO);
    }

    @Override
    public ProjectDTO getProjectById(UUID id) {
        Optional<ProjectEntity> projectById = projectRepository.findById(id);
        if (projectById.isPresent()) {
            return projectMapper.toDTO(projectById.get());
        } else {
            throw new NotFoundException(Messages.PROJECT_NOT_FOUND.getMsg() + id);
        }
    }

    @Override
    public ProjectDTO createProject(ProjectCreateRequestDTO projectCreateRequest) {
        ProjectEntity projectEntity = projectMapper.toEntity(projectCreateRequest);
        ProjectEntity savedProject = projectRepository.save(projectEntity);
        return projectMapper.toDTO(savedProject);
    }

    @Override
    public String deleteProjectById(UUID id) {
        Optional<ProjectEntity> projectById = projectRepository.findById(id);
        if (projectById.isPresent()) {
            projectRepository.delete(projectById.get());
            return "Project: " + projectById.get().getName() + " was deleted.";
        } else {
            throw new NotFoundException(Messages.PROJECT_NOT_FOUND.getMsg() + id);
        }
    }

    @Override
    public ProjectFilteringResponseDTO searchProject(
            ProjectFilteringRequestDTO projectFilteringRequestDTO) {

        if (projectFilteringRequestDTO == null) {
            projectFilteringRequestDTO = new ProjectFilteringRequestDTO();
        }

        Page<ProjectEntity> projectsFound =
                projectRepository.findAll(
                        ProjectSpecification.filterProject(projectFilteringRequestDTO),
                        pagingAndSortingMapper.toPageable(
                                projectFilteringRequestDTO.getPaging()));

        return ProjectFilteringResponseDTO.builder()
                .projects(projectMapper.toProjectDTOList(projectsFound.getContent()))
                .paging(
                        pagingAndSortingMapper.toPagingAndSortingMetadataDTO(projectsFound,
                                projectFilteringRequestDTO.getPaging()))
                .build();
    }
}
