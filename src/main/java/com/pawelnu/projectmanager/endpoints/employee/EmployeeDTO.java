package com.pawelnu.projectmanager.endpoints.employee;

import java.util.UUID;
import lombok.Data;

@Data
public class EmployeeDTO {

  private UUID id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String phoneNumber;
  private String companyName;
}
