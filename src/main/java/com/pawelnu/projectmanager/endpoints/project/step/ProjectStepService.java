package com.pawelnu.projectmanager.endpoints.project.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import com.pawelnu.projectmanager.endpoints.project.ProjectEntity;
import com.pawelnu.projectmanager.endpoints.project.ProjectService;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepListResponseDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectStepService {

  private final ProjectStepRepository projectStepRepository;
  private final ProjectStepQueryRepository projectStepQueryRepository;
  private final CategoryValueService categoryValueService;
  private final CompanyService companyService;
  private final EmployeeService employeeService;
  private final ProjectService projectService;
  private final ProjectStepMapper projectStepMapper;
  private final ObjectMapper objectMapper;

  public ProjectStepDTO create(ProjectStepCreateRequestDTO body) {

    ProjectEntity projectEntity = projectService.getProjectEntityById(body.getProjectId());
    EmployeeEntity employeeEntity =
        employeeService.getEmployeeEntityById(body.getAssignedEmployeeId());
    CategoryValueEntity projectStepPriority =
        categoryValueService.getCategoryValueById(body.getPriorityValueId());
    ProjectStepEntity projectStepEntity =
        ProjectStepEntity.builder()
            .name(body.getName())
            .project(projectEntity)
            .priority(projectStepPriority)
            .assignedEmployee(employeeEntity)
            .deadline(body.getDeadline())
            .build();
    ProjectStepEntity savedCompany = projectStepRepository.save(projectStepEntity);
    return projectStepMapper.toDTO(savedCompany);
  }

  // TODO implement filter
  public ProjectStepListResponseDTO filter(String sort, String range, String filter) {
    throw new NotImplementedException("no implementation!");
  }

  public ProjectStepDTO getById(UUID id) {
    return projectStepMapper.toDTO(getProjectStepEntityById(id));
  }

  // TODO implement editById
  public ProjectStepDTO editById(UUID id, ProjectStepEditRequestDTO body) {
    throw new NotImplementedException("no implementation!");
  }

  public SimpleResponse deleteById(UUID id) {
    ProjectStepEntity projectStepToDelete = getProjectStepEntityById(id);
    projectStepToDelete.setIsDeleted(true);
    ProjectStepEntity projectStepDeleted = projectStepRepository.save(projectStepToDelete);
    String item = "project step";
    if (projectStepDeleted.getIsDeleted()) {
      return SimpleResponse.builder().message(Shared.deleteMessage(item, id)).build();
    } else {
      return SimpleResponse.builder().message(Shared.cannotDeleteMessage(item, id)).build();
    }
  }

  private ProjectStepEntity getProjectStepEntityById(UUID id) {
    return projectStepRepository
        .findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new NotFoundException(MSG.PROJECT_STEP_NOT_FOUND + id));
  }
}
