package com.pawelnu.projectmanager.endpoints.authority;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class AuthorityDTO {

  private UUID id;
  @NotNull private String authority;
  //  private Set<EmployeeEntity> employees = new HashSet<>();
}
