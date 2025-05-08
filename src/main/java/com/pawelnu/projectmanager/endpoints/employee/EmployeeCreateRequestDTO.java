package com.pawelnu.projectmanager.endpoints.employee;

import com.pawelnu.projectmanager.entity.Auditable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeCreateRequestDTO extends Auditable {

  @NotNull private String firstName;
  @NotNull private String lastName;
  @NotNull private String username;

  @NotNull
  @Email
  @Size(min = 5, max = 255)
  private String email;

  @NotNull private String phoneNumber;
}
