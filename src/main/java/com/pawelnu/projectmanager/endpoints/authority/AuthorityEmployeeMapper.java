package com.pawelnu.projectmanager.endpoints.authority;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorityEmployeeMapper {

  @Mapping(source = "authority.name", target = "authorityName")
  @Mapping(source = "employee.username", target = "username")
  AddAuthorityToUserResponseDTO toDTO(EmployeeAuthorityEntity save);
}
