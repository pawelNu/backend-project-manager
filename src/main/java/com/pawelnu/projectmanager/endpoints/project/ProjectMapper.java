package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import com.pawelnu.projectmanager.endpoints.project.dto.ProjectDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

  @Mapping(source = "categoryValue.category.name", target = "categoryName")
  @Mapping(source = "categoryValue.id", target = "categoryValueId")
  @Mapping(source = "categoryValue.stringValue", target = "categoryValue")
  @Mapping(source = "company.id", target = "companyId")
  @Mapping(source = "company.name", target = "companyName")
  @Mapping(source = "assignedEmployee.id", target = "assignedEmployeeId")
  @Mapping(source = "assignedEmployee", target = "assignedEmployee")
  @Mapping(source = "priorityValue.category.name", target = "priorityName")
  @Mapping(source = "priorityValue.id", target = "priorityValueId")
  @Mapping(source = "priorityValue.stringValue", target = "priorityValue")
  ProjectDTO toDTO(ProjectEntity entity);

  default String employeeToString(EmployeeEntity entity) {
    if (entity == null) {
      return null;
    }
    return entity.getFirstName() + " " + entity.getLastName();
  }
}
