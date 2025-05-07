package com.pawelnu.projectmanager.endpoints.authority;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class AddAuthorityToUserRequestDTO {

  @NotNull private UUID authorityId;
  @NotNull private UUID userId;
}
