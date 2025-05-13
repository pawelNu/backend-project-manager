package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressDTO;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDTO {

  private UUID id;
  private String name;
  private String nip;
  private String regon;
  private String status;
  private String website;
  private List<CompanyAddressDTO> addresses;
}
