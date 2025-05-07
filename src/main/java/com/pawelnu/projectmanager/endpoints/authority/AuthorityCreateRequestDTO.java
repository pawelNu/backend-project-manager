package com.pawelnu.projectmanager.endpoints.authority;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthorityCreateRequestDTO {

  @NotNull private String authority;
}
