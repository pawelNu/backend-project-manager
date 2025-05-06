package com.pawelnu.projectmanager.endpoints.auth;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponse {
  private UUID id;
  private String jwtToken;
  private String username;
  private Date expireAt;
  private List<String> roles;
}
