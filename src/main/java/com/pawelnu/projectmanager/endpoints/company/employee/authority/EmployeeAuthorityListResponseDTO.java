package com.pawelnu.projectmanager.endpoints.company.employee.authority;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeAuthorityListResponseDTO {
  private List<EmployeeAuthorityDTO> data;
  private String contentRange;
}
