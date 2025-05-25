package com.pawelnu.projectmanager.endpoints.authority;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorityEditRequestDTO {
  @NotNull private String name;
}
