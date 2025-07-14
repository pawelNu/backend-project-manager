package com.pawelnu.projectmanager.endpoints.company.employee.authority.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeAuthorityIdNameDTO {
  private UUID id;
  private String nameBackend;
  private String nameFrontend;
}
