package com.pawelnu.projectmanager.endpoints.company.employee;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeAuthorityRowDTO {

  private UUID userId;
  private String username;
  private String email;
  private String password;
  private String authorityName;
}
