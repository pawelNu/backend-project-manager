package com.pawelnu.projectmanager.config.aop;

import com.pawelnu.projectmanager.config.security.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerSecurityAspect {

  @Before(
      "execution(* com.pawelnu.projectmanager.endpoints..*Controller.*(..)) && "
          + "!execution(* com.pawelnu.projectmanager.endpoints.auth.AuthController.*(..))")
  public void checkControllerAccess(JoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    String methodName = signature.getMethod().getName();
    String className = signature.getDeclaringType().getSimpleName();

    String baseName = className.replace("Controller", "");
    String authority = toUpperSnakeCase(baseName) + "_" + toUpperSnakeCase(methodName);

    log.debug("Checking authority: {}", authority);
    SecurityUtils.hasAuthority(authority);
  }

  private String toUpperSnakeCase(String input) {
    return input.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
  }
}
