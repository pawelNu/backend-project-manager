 package com.pawelnu.projectmanager.endpoints.project.step;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.project.step.dto.ProjectStepDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
 public interface ProjectStepMapper {
  
  @Mapping(source = "project.id", target = "projectId")
  @Mapping(source = "project.name", target = "projectName")
  @Mapping(source = "priority.id", target = "priorityId")
  @Mapping(source = "priority.stringValue", target = "priorityValue")
  @Mapping(source = "assignedEmployee.id", target = "assignedEmployeeId")
  @Mapping(source = "assignedEmployee", target = "assignedEmployee")
  ProjectStepDTO toDTO(ProjectStepEntity entity);

  default String employeeToString(EmployeeEntity entity) {
    if (entity == null) {
      return null;
    }
    return entity.getFirstName() + " " + entity.getLastName();
  }
 }
