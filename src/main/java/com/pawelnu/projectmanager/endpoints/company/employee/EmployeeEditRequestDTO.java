package com.pawelnu.projectmanager.endpoints.company.employee;

import java.util.UUID;
import lombok.Data;

@Data
public class EmployeeEditRequestDTO {

  private UUID id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String phoneNumber;
}
