package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeAuthorityMapper {

  @Mapping(source = "authority.name", target = "authorityName")
  @Mapping(source = "employee.username", target = "username")
  EmployeeAuthorityDTO toDTO(EmployeeAuthorityEntity save);

  default EmployeeAuthoritiesDTO toDTO(List<EmployeeAuthorityEntity> entities) {
    if (entities == null || entities.isEmpty()) {
      return null;
    }

    String username = entities.get(0).getEmployee().getUsername();

    List<String> authorityNames =
        entities.stream()
            .map(e -> e.getAuthority().getName())
            .distinct()
            .collect(Collectors.toList());

    return EmployeeAuthoritiesDTO.builder()
        .username(username)
        .authorityNames(authorityNames)
        .build();
  }
}
