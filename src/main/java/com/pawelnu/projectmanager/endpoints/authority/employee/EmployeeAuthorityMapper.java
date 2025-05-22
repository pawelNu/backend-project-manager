package com.pawelnu.projectmanager.endpoints.authority.employee;

import com.pawelnu.projectmanager.endpoints.authority.AddAuthorityToUserResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeAuthorityMapper {

  @Mapping(source = "authority.name", target = "authorityName")
  @Mapping(source = "employee.username", target = "username")
  AddAuthorityToUserResponseDTO toDTO(EmployeeAuthorityEntity save);
}
