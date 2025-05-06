package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.entity.Auditable;
import lombok.Data;

@Data
public class EmployeeCreateRequestDTO extends Auditable {

  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String phoneNumber;
}
