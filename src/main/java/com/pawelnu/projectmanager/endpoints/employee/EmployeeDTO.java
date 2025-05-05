package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.entity.Auditable;
import java.util.UUID;
import lombok.Data;

@Data
public class EmployeeDTO extends Auditable {

  private UUID id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String phoneNumber;
}
