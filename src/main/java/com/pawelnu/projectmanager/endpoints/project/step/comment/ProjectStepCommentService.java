package com.pawelnu.projectmanager.endpoints.project.step.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pawelnu.projectmanager.endpoints.category.value.CategoryValueService;
import com.pawelnu.projectmanager.endpoints.company.CompanyService;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeService;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepEntity;
import com.pawelnu.projectmanager.endpoints.project.step.ProjectStepService;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentCreateRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentDTO;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentEditRequestDTO;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentListResponseDTO;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentRowDTO;
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

  private final ProjectStepCommentRepository projectStepCommentRepository;
  private final ProjectStepCommentQueryRepository projectStepCommentQueryRepository;
  private final CategoryValueService categoryValueService;
  private final CompanyService companyService;
  private final EmployeeService employeeService;
  private final ProjectStepService projectStepService;
  private final ProjectStepCommentMapper projectStepCommentMapper;
  private final ObjectMapper objectMapper;

  public ProjectStepCommentDTO create(ProjectStepCommentCreateRequestDTO body) {
    //    private String comment;
    //    private UUID projectStepId;
    //    private UUID employeeId;
    ProjectStepEntity projectStepEntity =
        projectStepService.getProjectStepEntityById(body.getProjectStepId());
    EmployeeEntity employeeEntity = employeeService.getEmployeeEntityById(body.getEmployeeId());
    ProjectStepCommentEntity projectStepCommentEntity =
        ProjectStepCommentEntity.builder()
            .comment(body.getComment())
            .step(projectStepEntity)
            .employee(employeeEntity)
            .build();
    ProjectStepCommentEntity savedCompany =
        projectStepCommentRepository.save(projectStepCommentEntity);
    return projectStepCommentMapper.toDTO(savedCompany);
  }

  public ProjectStepCommentListResponseDTO filter(String sort, String range, String filter) {
    PageableParams params = Shared.preparePageableParams(objectMapper, sort, range, filter);

    List<ProjectStepCommentRowDTO> results = projectStepCommentQueryRepository.getList(params);
    List<ProjectStepCommentDTO> projectDTOs =
        results.stream().map(projectStepCommentMapper::toDTO).toList();
    String contentRange =
        Shared.prepareContentRange(
            results.isEmpty() ? 0 : results.getFirst().getTotalElements(),
            params.getOffset(),
            params.getLimit());
    return ProjectStepCommentListResponseDTO.builder()
        .data(projectDTOs)
        .contentRange(contentRange)
        .build();
  }

  public ProjectStepCommentDTO getById(UUID id) {
    return projectStepCommentMapper.toDTO(getProjectStepCommentEntityById(id));
  }

  public ProjectStepCommentDTO editById(UUID id, ProjectStepCommentEditRequestDTO body) {
    ProjectStepCommentEntity projectStepCommentToEdit = getProjectStepCommentEntityById(id);
    ProjectStepEntity projectStep = projectStepService.getProjectStepEntityById(body.getStepId());
    EmployeeEntity employeeEntity = employeeService.getEmployeeEntityById(body.getEmployeeId());
    projectStepCommentToEdit.setComment(body.getComment());
    projectStepCommentToEdit.setStep(projectStep);
    projectStepCommentToEdit.setEmployee(employeeEntity);
    ProjectStepCommentEntity updatedProject =
        projectStepCommentRepository.save(projectStepCommentToEdit);
    return projectStepCommentMapper.toDTO(updatedProject);
  }

  public SimpleResponse deleteById(UUID id) {
    ProjectStepCommentEntity projectStepToDelete = getProjectStepCommentEntityById(id);
    projectStepToDelete.setIsDeleted(true);
    ProjectStepCommentEntity projectStepDeleted =
        projectStepCommentRepository.save(projectStepToDelete);
    String item = "project step comment";
    if (projectStepDeleted.getIsDeleted()) {
      return SimpleResponse.builder().message(Shared.deleteMessage(item, id)).build();
    } else {
      return SimpleResponse.builder().message(Shared.cannotDeleteMessage(item, id)).build();
    }
  }

  private ProjectStepCommentEntity getProjectStepCommentEntityById(UUID id) {
    return projectStepCommentRepository
        .findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new NotFoundException(MSG.PROJECT_STEP_COMMENT_NOT_FOUND + id));
  }
}
