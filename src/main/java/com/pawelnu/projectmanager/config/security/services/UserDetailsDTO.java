package com.pawelnu.projectmanager.config.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.UUID;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
public class UserDetailsDTO {
  private UUID id;
  private String username;
  private String email;
  @JsonIgnore private String password;
  private Collection<? extends GrantedAuthority> authorities;
}
