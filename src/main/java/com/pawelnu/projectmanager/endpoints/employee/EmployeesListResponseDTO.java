package com.pawelnu.projectmanager.endpoints.employee;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeesListResponseDTO {

  private List<EmployeeDTO> data;
  private long totalElements;
  private int start;
  private long end;
}
