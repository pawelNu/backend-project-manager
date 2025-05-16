package com.pawelnu.projectmanager.endpoints.company.address;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyAddressesListResponseDTO {

  private List<CompanyAddressDTO> data;
  private long totalElements;
  private int start;
  private long end;
}
