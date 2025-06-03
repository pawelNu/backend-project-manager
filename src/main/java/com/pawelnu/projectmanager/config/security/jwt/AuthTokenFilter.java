package com.pawelnu.projectmanager.config.security.jwt;

import com.pawelnu.projectmanager.config.security.AuthorityChecker;
import com.pawelnu.projectmanager.config.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

@RequiredArgsConstructor
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;
  private final UserDetailsServiceImpl userDetailsService;
  private final List<HandlerMapping> handlerMappings;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    log.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());
    String jwt = parseJwt(request);
    if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
      String username = jwtUtils.getUserNameFromJwtHeaderToken(jwt);

      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      log.info(userDetails.toString());

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
      log.debug("Roles from JWT: {}", userDetails.getAuthorities());
      authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authentication);
      HandlerMethod handlerMethod = getHandlerMethod(request);
      if (handlerMethod != null) {
        String className = handlerMethod.getBeanType().getSimpleName();
        String methodName = handlerMethod.getMethod().getName();
        String authority = prepareAuthorityString(className, methodName);
        AuthorityChecker.hasAuthority(authority);
      } else {
        log.warn("HandlerMethod not found for request URI: {}", request.getRequestURI());
      }
    }
    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String jwt = jwtUtils.getJwtFromHeader(request);
    log.debug("AuthTokenFilter.java: {}", jwt);
    return jwt;
  }

  private HandlerMethod getHandlerMethod(HttpServletRequest request) {
    try {
      for (HandlerMapping mapping : handlerMappings) {
        var handler = mapping.getHandler(request);
        if (handler != null && handler.getHandler() instanceof HandlerMethod) {
          return (HandlerMethod) handler.getHandler();
        }
      }
    } catch (Exception e) {
      log.error("Error getting HandlerMethod for request", e);
    }
    return null;
  }

  public static String prepareAuthorityString(String className, String methodName) {
    String baseName = className.replace("Controller", "");
    return toUpperSnakeCase(baseName) + "_" + toUpperSnakeCase(methodName);
  }

  private static String toUpperSnakeCase(String input) {
    return input.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
  }
}
