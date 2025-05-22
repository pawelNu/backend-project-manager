package com.pawelnu.projectmanager.endpoints.company;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySimpleDTO {

  private UUID id;
  private String name;
  private String nip;
  private String regon;
  private String status;
  private String website;
}
