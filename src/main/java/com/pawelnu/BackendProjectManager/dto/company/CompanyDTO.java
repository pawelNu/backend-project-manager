package com.pawelnu.BackendProjectManager.dto.company;

import java.util.UUID;
import lombok.Data;

@Data
public class CompanyDTO {

  private UUID id;
  private String name;
  private String status;
}
