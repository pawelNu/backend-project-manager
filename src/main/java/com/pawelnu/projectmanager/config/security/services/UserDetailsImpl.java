package com.pawelnu.projectmanager.config.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pawelnu.projectmanager.endpoints.employee.EmployeeEntity;
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
  private Collection<? extends GrantedAuthority> authorities;

  public static UserDetailsImpl build(EmployeeEntity user) {
    List<GrantedAuthority> authorities =
        user.getAuthorities().stream()
            .map(role -> new SimpleGrantedAuthority(role.getAuthority().getName()))
            .collect(Collectors.toList());

    return new UserDetailsImpl(
        user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
  }
}
