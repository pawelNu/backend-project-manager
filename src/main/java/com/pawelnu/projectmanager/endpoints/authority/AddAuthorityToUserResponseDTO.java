package com.pawelnu.projectmanager.endpoints.authority;

import lombok.Data;

@Data
public class AddAuthorityToUserResponseDTO {

  private String username;
  private String authorityName;
}
