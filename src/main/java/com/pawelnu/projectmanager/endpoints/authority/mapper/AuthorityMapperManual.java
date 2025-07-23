package com.pawelnu.projectmanager.endpoints.authority.mapper;

import com.pawelnu.projectmanager.endpoints.authority.AuthorityEntity;
import com.pawelnu.projectmanager.endpoints.authority.dto.AuthorityDTO;
import com.pawelnu.projectmanager.endpoints.company.employee.EmployeeMapper;
import com.pawelnu.projectmanager.endpoints.company.employee.authority.EmployeeAuthorityEntity;
import com.pawelnu.projectmanager.endpoints.company.employee.dto.EmployeeSimpleDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorityMapperManual {
  private final EmployeeMapper employeeMapper;

  public AuthorityDTO toDTO(AuthorityEntity entity) {
    List<EmployeeSimpleDTO> employees =
        entity.getEmployeeAuthorities().stream()
            .map(EmployeeAuthorityEntity::getEmployee)
            .toList()
            .stream()
            .map(employeeMapper::toSimpleDTO)
            .toList();
    return AuthorityDTO.builder()
        .id(entity.getId())
        .nameBackend(entity.getNameBackend())
        .nameFrontend(entity.getNameFrontend())
        .employees(employees)
        .build();
  }
}
