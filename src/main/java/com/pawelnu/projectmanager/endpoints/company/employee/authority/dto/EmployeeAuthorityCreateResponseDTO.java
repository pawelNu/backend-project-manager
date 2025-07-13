package com.pawelnu.projectmanager.endpoints.company.employee.authority.dto;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeAuthorityCreateResponseDTO {
  private UUID id;
  private String username;
  private List<EmployeeAuthorityIdNameDTO> employeeAuthorities;
}
