package com.pawelnu.projectmanager.endpoints.company;

import java.util.UUID;
import lombok.Data;

@Data
public class CompanySimpleDTO {

  private UUID id;
  private String name;
  private String nip;
  private String regon;
  private String status;
  private String website;
}
