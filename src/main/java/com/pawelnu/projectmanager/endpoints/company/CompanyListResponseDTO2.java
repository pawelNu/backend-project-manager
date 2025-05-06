package com.pawelnu.projectmanager.endpoints.company;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyListResponseDTO2 {

  private List<CompanySimpleDTO> data;
  private long totalElements;
  private int start;
  private long end;
}
