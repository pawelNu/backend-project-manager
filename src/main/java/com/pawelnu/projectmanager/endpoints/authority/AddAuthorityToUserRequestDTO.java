package com.pawelnu.projectmanager.endpoints.authority;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddAuthorityToUserRequestDTO {

  @NotNull private UUID authorityId;
  @NotNull private UUID employeeId;
}
