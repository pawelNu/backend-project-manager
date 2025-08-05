package com.pawelnu.projectmanager.endpoints.project.step.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueEntity;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import com.pawelnu.projectmanager.endpoints.project.ProjectEntity;
import com.pawelnu.projectmanager.endpoints.project.ProjectService;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepEntity;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepRepository;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepListResponseDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepRowDTO;
import com.pawelnu.projectmanager.exception.NotFoundException;
import com.pawelnu.projectmanager.exception.model.SimpleResponse;
import com.pawelnu.projectmanager.utils.Consts.MSG;
import com.pawelnu.projectmanager.utils.PageableParams;
import com.pawelnu.projectmanager.utils.Shared;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProjectStepCommentService {

  private final ProjectStepRepository projectStepRepository;
  private final ProjectStepCommentQueryRepository projectStepCommentQueryRepository;
  private final CategoryValueService categoryValueService;
  private final CompanyService companyService;
  private final EmployeeService employeeService;
  private final ProjectService projectService;
  private final ProjectStepCommentMapper projectStepCommentMapper;
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
    return projectStepCommentMapper.toDTO(savedCompany);
  }

  public ProjectStepListResponseDTO filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    List<ProjectStepRowDTO> results = projectStepCommentQueryRepository.getList(params);
    List<ProjectStepDTO> projectDTOs =
        results.stream().map(projectStepCommentMapper::toDTO).toList();
    String contentRange =
        Shared.prepareContentRange(
            results.isEmpty() ? 0 : results.getFirst().getTotalElements(),
            params.getOffset(),
            params.getLimit());
    return ProjectStepListResponseDTO.builder()
        .data(projectDTOs)
        .contentRange(contentRange)
        .build();
  }

  public ProjectStepDTO getById(UUID id) {
    return projectStepCommentMapper.toDTO(getProjectStepEntityById(id));
  }

  public ProjectStepDTO editById(UUID id, ProjectStepEditRequestDTO body) {
    ProjectStepEntity projectStepToEdit = getProjectStepEntityById(id);
    ProjectEntity project = projectService.getProjectEntityById(body.getProjectId());
    EmployeeEntity employeeEntity =
        employeeService.getEmployeeEntityById(body.getAssignedEmployeeId());
    CategoryValueEntity projectStepPriority =
        categoryValueService.getCategoryValueById(body.getPriorityValueId());
    projectStepToEdit.setName(body.getName());
    projectStepToEdit.setProject(project);
    projectStepToEdit.setPriority(projectStepPriority);
    projectStepToEdit.setAssignedEmployee(employeeEntity);
    projectStepToEdit.setDeadline(body.getDeadline());
    ProjectStepEntity updatedProject = projectStepRepository.save(projectStepToEdit);
    return projectStepCommentMapper.toDTO(updatedProject);
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
