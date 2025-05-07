package com.pawelnu.projectmanager.endpoints.authority;

import com.pawelnu.projectmanager.endpoints.employee.EmployeeEntity;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class AuthorityDTO {

  private UUID id;
  @NotNull
  private String authority;
//  private Set<EmployeeEntity> employees = new HashSet<>();
}
