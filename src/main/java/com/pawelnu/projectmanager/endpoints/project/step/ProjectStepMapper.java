package com.pawelnu.projectmanager.endpoints.project.step;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepDTO;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepRowDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProjectStepMapper {

  @Mapping(source = "project.id", target = "projectId")
  @Mapping(source = "project.name", target = "projectName")
  @Mapping(source = "priority.id", target = "priorityId")
  @Mapping(source = "priority.stringValue", target = "priorityValue")
  @Mapping(source = "assignedEmployee.id", target = "assignedEmployeeId")
  @Mapping(source = "assignedEmployee", target = "assignedEmployee")
  ProjectStepDTO toDTO(ProjectStepEntity entity);

  @Mapping(target = "assignedEmployee", source = ".", qualifiedByName = "concatEmployeeName")
  ProjectStepDTO toDTO(ProjectStepRowDTO row);


  default String employeeToString(EmployeeEntity entity) {
    if (entity == null) {
      return null;
    }
    return entity.getFirstName() + " " + entity.getLastName();
  }

  @Named("concatEmployeeName")
  default String concatEmployeeName(ProjectStepRowDTO row) {
    if (row == null) {
      return null;
    }
    return row.getEmployeeFirstName() + " " + row.getEmployeeLastName();
  }
}
