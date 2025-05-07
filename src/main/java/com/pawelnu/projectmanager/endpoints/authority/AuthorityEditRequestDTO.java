package com.pawelnu.projectmanager.endpoints.authority;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthorityEditRequestDTO {
  @NotNull
  private String authority;
}
