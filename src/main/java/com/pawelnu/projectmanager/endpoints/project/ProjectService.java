package com.pawelnu.projectmanager.endpoints.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.company.CompanyEntity;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectQueryRepository projectQueryRepository;
  private final CategoryValueService categoryValueService;
  private final CompanyService companyService;
  private final EmployeeService employeeService;
  private final ProjectMapper projectMapper;
  private final ObjectMapper objectMapper;

  public ProjectDTO create(ProjectCreateRequestDTO body) {
    CategoryValueEntity projectCategory =
        categoryValueService.findCategoryByNameAndValue("project category", "production");
    CompanyEntity companyEntity = companyService.getCompanyEntityById(body.getCompanyId());
    EmployeeEntity employeeEntity =
        employeeService.getEmployeeEntityById(body.getAssignedEmployeeId());
    CategoryValueEntity projectPriority =
        categoryValueService.findCategoryByNameAndValue("project priority", "5");
    ProjectEntity projectEntity = projectMapper.toEntity(body);
    projectEntity.setCategoryValue(projectCategory);
    projectEntity.setCompany(companyEntity);
    projectEntity.setAssignedEmployee(employeeEntity);
    projectEntity.setPriorityValue(projectPriority);
    ProjectEntity savedCompany = projectRepository.save(projectEntity);
    return projectMapper.toDTO(savedCompany);
  }

  public ProjectDTO getById(UUID id) {
    ProjectEntity companyEntity = getProjectEntityById(id);
    return projectMapper.toDTO(companyEntity);
  }

  public ProjectEntity getProjectEntityById(UUID id) {
    return projectQueryRepository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(MSG.PROJECT_NOT_FOUND + id));
  }

  public SimpleResponse deleteById(UUID id) {
    ProjectEntity projectToDelete = getProjectEntityById(id);
    projectToDelete.setIsDeleted(true);
    ProjectEntity projectDeleted = projectRepository.save(projectToDelete);
    if (projectDeleted.getIsDeleted()) {
      return SimpleResponse.builder().message("Deleted project with id: " + id).build();
    } else {
      return SimpleResponse.builder().message("Cannot delete project with id: " + id).build();
    }
  }

  public ProjectDTO editById(UUID id, ProjectEditRequestDTO body) {
    ProjectEntity projectToEdit = getProjectEntityById(id);
    projectMapper.toEntity(body, projectToEdit);
    ProjectEntity updatedProject = projectRepository.save(projectToEdit);
    return projectMapper.toDTO(updatedProject);
  }
}
