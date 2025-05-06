package com.pawelnu.projectmanager.endpoints.employee;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  @Mapping(target = "id", ignore = true)
  EmployeeEntity toEntity(EmployeeCreateRequestDTO dto);

  EmployeeDTO toDTO(EmployeeEntity entity);
}
