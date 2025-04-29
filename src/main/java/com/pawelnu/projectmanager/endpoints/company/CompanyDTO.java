package com.pawelnu.projectmanager.endpoints.company;

import com.pawelnu.projectmanager.endpoints.companyaddress.CompanyAddressDTO;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class CompanyDTO {

  private UUID id;
  private String name;
  private String nip;
  private String regon;
  private String website;
  private List<CompanyAddressDTO> addresses;
}
