package com.pawelnu.projectmanager.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtils {

  public static void hasAuthority(String authority) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null
        || auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .noneMatch(a -> a.equals(authority))) {
      log.error("Missing required authority: {}", authority);
      throw new AccessDeniedException("Access denied");
    }
  }

  public static void checkAuthority() {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

    StackTraceElement caller = null;
    for (StackTraceElement element : stackTrace) {
      if (!element.getClassName().equals(SecurityUtils.class.getName())
          && !element.getClassName().startsWith("java.lang.Thread")) {
        caller = element;
        break;
      }
    }

    if (caller == null) {
      throw new IllegalStateException("Cannot find caller place");
    }

    String fullClassName = caller.getClassName();
    String methodName = caller.getMethodName();

    String simpleClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
    String baseName = simpleClassName.replace("Controller", "");

    String requiredAuthority = baseName + "_" + methodName;

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null
        || auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .noneMatch(a -> a.equals(requiredAuthority))) {
      log.error("Missing required authority: {}", requiredAuthority);
      throw new AccessDeniedException("Access denied");
    }
  }
}
