package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeAuthorityDeleteRequestDTO {
  @NotNull private UUID employeeId;
  @NotNull private List<UUID> authorityIds;
}
