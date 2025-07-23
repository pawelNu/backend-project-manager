package com.pawelnu.projectmanager.endpoints.authority.dto;

import com.pawelnu.projectmanager.endpoints.company.employee.dto.EmployeeSimpleDTO;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthorityDTO {

  private UUID id;
  private String nameBackend;
  private String nameFrontend;
  private List<EmployeeSimpleDTO> employees;
}
