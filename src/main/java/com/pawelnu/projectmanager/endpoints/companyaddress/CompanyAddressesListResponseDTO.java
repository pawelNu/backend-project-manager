package com.pawelnu.projectmanager.endpoints.companyaddress;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyAddressesListResponseDTO {

  private List<CompanyAddressDTO> data;
  public String contentRange;
}
