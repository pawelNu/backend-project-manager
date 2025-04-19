// package com.pawelnu.BackendProjectManager.service.impl;
//
// import com.pawelnu.BackendProjectManager.dto.PagingAndSortingRequestDTO;
// import com.pawelnu.BackendProjectManager.dto.project.ProjectCreateRequestDTO;
// import com.pawelnu.BackendProjectManager.dto.project.ProjectDTO;
// import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringRequestDTO;
// import com.pawelnu.BackendProjectManager.dto.project.ProjectFilteringResponseDTO;
// import com.pawelnu.BackendProjectManager.entity.ProjectEntity;
// import com.pawelnu.BackendProjectManager.enums.Messages;
// import com.pawelnu.BackendProjectManager.exception.NotFoundException;
// import com.pawelnu.BackendProjectManager.mapper.PagingAndSortingMapper;
// import com.pawelnu.BackendProjectManager.mapper.ProjectMapper;
// import com.pawelnu.BackendProjectManager.repository.ProjectRepository;
// import com.pawelnu.BackendProjectManager.repository.project.ProjectSpecification;
// import com.pawelnu.BackendProjectManager.service.ProjectService;
// import java.util.Optional;
// import java.util.UUID;
// import lombok.AllArgsConstructor;
// import org.springframework.data.domain.Page;
// import org.springframework.stereotype.Service;
//
// @Service
// @AllArgsConstructor
// public class ProjectServiceImpl implements ProjectService {
//
//  private final ProjectMapper projectMapper;
//  private final ProjectRepository projectRepository;
//  private final PagingAndSortingMapper pagingAndSortingMapper;
//
//  @Override
//  public ProjectFilteringResponseDTO getAllProjects(
//      Integer pageNumber, Integer pageSize, String sortingField, Boolean isAscendingSorting) {
//
//    PagingAndSortingRequestDTO requestDTO = new PagingAndSortingRequestDTO();
//    requestDTO.setPageSize(pageSize);
//    requestDTO.setPageNumber(pageNumber);
//    requestDTO.setSortingField(sortingField);
//    requestDTO.setIsAscendingSorting(isAscendingSorting);
//
//    ProjectFilteringRequestDTO filterRequestDTO = new ProjectFilteringRequestDTO();
//    filterRequestDTO.setPaging(requestDTO);
//
//    Page<ProjectEntity> projectsFound =
//        projectRepository.findAll(
//            ProjectSpecification.filterProject(filterRequestDTO),
//            pagingAndSortingMapper.toPageable(filterRequestDTO.getPaging()));
//
//    return ProjectFilteringResponseDTO.builder()
//        .projects(projectMapper.toProjectDTOList(projectsFound.getContent()))
//        .paging(
//            pagingAndSortingMapper.toPagingAndSortingMetadataDTO(
//                projectsFound, filterRequestDTO.getPaging()))
//        .build();
//  }
//
//  @Override
//  public ProjectDTO getProjectById(UUID id) {
//    Optional<ProjectEntity> projectById = projectRepository.findById(id);
//    if (projectById.isPresent()) {
//      return projectMapper.toDTO(projectById.get());
//    } else {
//      throw new NotFoundException(Messages.PROJECT_NOT_FOUND.getMsg() + id);
//    }
//  }
//
//  @Override
//  public ProjectDTO createProject(ProjectCreateRequestDTO projectCreateRequest) {
//    ProjectEntity projectEntity = projectMapper.toEntity(projectCreateRequest);
//    ProjectEntity savedProject = projectRepository.save(projectEntity);
//    return projectMapper.toDTO(savedProject);
//  }
//
//  @Override
//  public String deleteProjectById(UUID id) {
//    Optional<ProjectEntity> projectById = projectRepository.findById(id);
//    if (projectById.isPresent()) {
//      projectRepository.delete(projectById.get());
//      return "Project: " + projectById.get().getName() + " was deleted.";
//    } else {
//      throw new NotFoundException(Messages.PROJECT_NOT_FOUND.getMsg() + id);
//    }
//  }
//
//  @Override
//  public ProjectFilteringResponseDTO searchProject(
//      ProjectFilteringRequestDTO projectFilteringRequestDTO) {
//
//    if (projectFilteringRequestDTO == null) {
//      projectFilteringRequestDTO = new ProjectFilteringRequestDTO();
//    }
//
//    Page<ProjectEntity> projectsFound =
//        projectRepository.findAll(
//            ProjectSpecification.filterProject(projectFilteringRequestDTO),
//            pagingAndSortingMapper.toPageable(projectFilteringRequestDTO.getPaging()));
//
//    return ProjectFilteringResponseDTO.builder()
//        .projects(projectMapper.toProjectDTOList(projectsFound.getContent()))
//        .paging(
//            pagingAndSortingMapper.toPagingAndSortingMetadataDTO(
//                projectsFound, projectFilteringRequestDTO.getPaging()))
//        .build();
//  }
// }
