package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeAuthorityDTO {
  private UUID id;
  private String username;
  private String authorityName;
  private String employeeFirstName;
  private String employeeLastName;
}
