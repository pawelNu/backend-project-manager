package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeAuthorityDTO {
  private String username;
  private String authorityName;
}
