package com.pawelnu.projectmanager.endpoints.company.employee.authority.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeAuthoritiesDTO {
  private String username;
  private List<String> authorityNames;
}
