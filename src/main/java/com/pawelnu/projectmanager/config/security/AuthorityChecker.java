package com.pawelnu.projectmanager.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorityChecker {

  public static void hasAuthority(String authority) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean hasAuthority =
        auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(a -> a.equals(authority));
    if (auth == null || !hasAuthority) {
      log.error("Missing required authority: {}", authority);
      throw new AccessDeniedException("Access denied");
    }
  }
}
