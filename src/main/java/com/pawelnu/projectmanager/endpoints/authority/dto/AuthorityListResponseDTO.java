package com.pawelnu.projectmanager.endpoints.authority.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorityListResponseDTO {

  private List<AuthorityDTO> data;
  private String contentRange;
}
