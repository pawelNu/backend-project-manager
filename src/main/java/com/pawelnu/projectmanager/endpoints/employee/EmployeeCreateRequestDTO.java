package com.pawelnu.projectmanager.endpoints.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Data;

@Data
public class EmployeeCreateRequestDTO {

  @NotNull private UUID companyId;
  @NotNull private String firstName;
  @NotNull private String lastName;
  @NotNull private String username;

  @NotNull
  @Email
  @Size(min = 5, max = 255)
  private String email;

  @NotNull private String phoneNumber;
}
