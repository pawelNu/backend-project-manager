package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
  @Mapping(target = "id", ignore = true)
  ProjectEntity toEntity(ProjectCreateRequestDTO companyCreateRequest);

  @Mapping(source = "categoryValue.category.name", target = "categoryName")
  @Mapping(source = "categoryValue.stringValue", target = "categoryValue")
  @Mapping(source = "company.name", target = "companyName")
  @Mapping(source = "assignedEmployee", target = "assignedEmployee")
  @Mapping(source = "priorityValue.category.name", target = "priorityName")
  @Mapping(source = "priorityValue.stringValue", target = "priorityValue")
  ProjectDTO toDTO(ProjectEntity companyEntity);

  default String employeeToString(EmployeeEntity employee) {
    if (employee == null) {
      return null;
    }
    return employee.getFirstName() + " " + employee.getLastName();
  }
}
