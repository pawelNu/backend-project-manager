package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityCreateResponseDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.dto.EmployeeAuthorityIdNameDTO;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeAuthorityMapper {

  @Mapping(source = "authority.name", target = "authorityName")
  @Mapping(source = "employee.username", target = "username")
  @Mapping(source = "employee.firstName", target = "employeeFirstName")
  @Mapping(source = "employee.lastName", target = "employeeLastName")
  EmployeeAuthorityDTO toDTO(EmployeeAuthorityEntity save);
  
  @Mapping(source = "authority.name", target = "name")
  EmployeeAuthorityIdNameDTO toIdNameDTO(EmployeeAuthorityEntity save);

  default EmployeeAuthorityCreateResponseDTO toDTO(List<EmployeeAuthorityEntity> entities) {
    if (entities == null || entities.isEmpty()) {
      return null;
    }

    UUID employeeId = entities.getFirst().getEmployee().getId();
    String username = entities.getFirst().getEmployee().getUsername();

    List<EmployeeAuthorityIdNameDTO> employeeAuthorityDTOs =
        entities.stream()
            .map(this::toIdNameDTO)
            .toList();

    return EmployeeAuthorityCreateResponseDTO.builder()
        .id(employeeId)
        .username(username)
        .employeeAuthorities(employeeAuthorityDTOs)
        .build();
  }
}
