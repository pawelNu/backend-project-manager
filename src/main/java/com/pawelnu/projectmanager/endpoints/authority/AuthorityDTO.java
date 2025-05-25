package com.pawelnu.projectmanager.endpoints.authority;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorityDTO {

  private UUID id;
  private String name;
}
