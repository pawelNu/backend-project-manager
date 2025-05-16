package com.pawelnu.projectmanager.endpoints.company.employee;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeRowDTO {

  private UUID userId;
  private String username;
  private String email;
  private String password;
  private List<String> authorities;
}
