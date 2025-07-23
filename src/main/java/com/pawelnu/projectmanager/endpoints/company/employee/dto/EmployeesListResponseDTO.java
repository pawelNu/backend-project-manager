package com.pawelnu.projectmanager.endpoints.company.employee.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeesListResponseDTO {

  private List<EmployeeDTO> data;
  private String contentRange;
}
