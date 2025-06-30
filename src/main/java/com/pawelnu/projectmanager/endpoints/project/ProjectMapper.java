package com.pawelnu.projectmanager.endpoints.project;

import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
  @Mapping(target = "id", ignore = true)
  ProjectEntity toEntity(ProjectCreateRequestDTO dto);

  @Mapping(target = "id", ignore = true)
  ProjectEntity toEntity(ProjectEditRequestDTO dto, @MappingTarget ProjectEntity entity);

  @Mapping(source = "categoryValue.category.name", target = "categoryName")
  @Mapping(source = "categoryValue.stringValue", target = "categoryValue")
  @Mapping(source = "company.name", target = "companyName")
  @Mapping(source = "assignedEmployee", target = "assignedEmployee")
  @Mapping(source = "priorityValue.category.name", target = "priorityName")
  @Mapping(source = "priorityValue.stringValue", target = "priorityValue")
  ProjectDTO toDTO(ProjectEntity entity);

  default String employeeToString(EmployeeEntity entity) {
    if (entity == null) {
      return null;
    }
    return entity.getFirstName() + " " + entity.getLastName();
  }
}
