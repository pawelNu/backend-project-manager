package com.pawelnu.projectmanager.endpoints.project.step.comment;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentDTO;
import com.pawelnu.projectmanager.endpoints.project.step.comment.dto.ProjectStepCommentRowDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProjectStepCommentMapper {

  @Mapping(source = "step.project.id", target = "projectId")
  @Mapping(source = "step.project.name", target = "projectName")
  @Mapping(source = "step.id", target = "stepId")
  @Mapping(source = "step.name", target = "stepName")
  @Mapping(source = "employee.id", target = "employeeId")
  @Mapping(source = "employee", target = "employeeName", qualifiedByName = "employeeToString")
  ProjectStepCommentDTO toDTO(ProjectStepCommentEntity entity);

  @Mapping(source = ".", target = "employeeName", qualifiedByName = "concatEmployeeName")
  ProjectStepCommentDTO toDTO(ProjectStepCommentRowDTO row);

  @Named("employeeToString")
  static String employeeToString(EmployeeEntity employee) {
    if (employee == null) {
      return null;
    }
    return employee.getFirstName() + " " + employee.getLastName();
  }

  @Named("concatEmployeeName")
  default String concatEmployeeName(ProjectStepCommentRowDTO row) {
    if (row == null) {
      return null;
    }
    return row.getEmployeeFirstName() + " " + row.getEmployeeLastName();
  }
}
