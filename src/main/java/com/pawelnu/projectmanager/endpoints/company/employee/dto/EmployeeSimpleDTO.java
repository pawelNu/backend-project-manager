package com.pawelnu.projectmanager.endpoints.company.employee.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class EmployeeSimpleDTO {

  private UUID id;
  private String firstName;
  private String lastName;
  private String username;
  private String companyName;
}
