package com.pawelnu.projectmanager.config.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

  private UUID id;
  private String username;
  private String email;
  @JsonIgnore private String password;
  private Collection<? extends GrantedAuthority> backendAuthorities;
  private List<String> frontendAuthorities;

  public static UserDetailsImpl build(UserDetailsDTO user) {
    List<GrantedAuthority> backendAuthorities = getBackendAuthorities(user);
    List<String> frontendAuthorities = getFrontendAuthorities(user);

    return new UserDetailsImpl(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getPassword(),
        backendAuthorities,
        frontendAuthorities);
  }

  private static List<String> getFrontendAuthorities(UserDetailsDTO user) {
    return user.getFrontendAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();
  }

  private static List<GrantedAuthority> getBackendAuthorities(UserDetailsDTO user) {
    return user.getBackendAuthorities().stream()
        .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
        .collect(Collectors.toList());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.backendAuthorities;
  }
}
