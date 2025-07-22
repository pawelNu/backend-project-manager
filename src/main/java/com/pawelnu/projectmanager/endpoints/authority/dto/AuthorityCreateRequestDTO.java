package com.pawelnu.projectmanager.endpoints.authority.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorityCreateRequestDTO {

  @NotNull
  @Size(min = 5, max = 255, message = "Name should has 5-255 characters")
  private String nameBackend;

  @Size(min = 5, max = 255, message = "Name should has 5-255 characters")
  private String nameFrontend;
}
