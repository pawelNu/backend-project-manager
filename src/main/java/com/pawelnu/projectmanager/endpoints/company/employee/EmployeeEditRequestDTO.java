package com.pawelnu.projectmanager.endpoints.company.employee;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeEditRequestDTO {

  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String phoneNumber;
}
