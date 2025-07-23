package com.pawelnu.projectmanager.utils;

import com.pawelnu.projectmanager.config.security.jwt.JwtUtils;
import com.pawelnu.projectmanager.exception.model.ReactAdminError;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public class Utils {
  public static final String FULL_AUTH_IS_REQUIRED =
      "Full authentication is required to access this resource";
  public static final String INVALID_UUID =
      "Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type"
          + " 'java.util.UUID'; Invalid UUID string: invalid-uuid";

  public static String TOKEN_WITH_AUTH;
  public static String TOKEN_WITHOUT_AUTH;

  public static RequestPostProcessor withJwt() {
    return withToken(TOKEN_WITH_AUTH);
  }

  public static RequestPostProcessor withBadJwt() {
    return withToken(TOKEN_WITHOUT_AUTH);
  }

  public static void generateToken(JwtUtils jwtUtils) {
    if (Utils.TOKEN_WITH_AUTH == null) {
      Utils.TOKEN_WITH_AUTH = jwtUtils.generateTokenFromUsername("test");
    }
    if (Utils.TOKEN_WITHOUT_AUTH == null) {
      Utils.TOKEN_WITHOUT_AUTH = jwtUtils.generateTokenFromUsername("user_with_no_authorities");
    }
  }

  private static RequestPostProcessor withToken(String token) {
    return request -> {
      request.addHeader("Authorization", "Bearer " + token);
      request.addHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
      return request;
    };
  }

  @NotNull
  public static ReactAdminError accessDeniedError() {
    return new ReactAdminError("Access denied");
  }

  public static ReactAdminError unauthorizedError() {
    return new ReactAdminError(FULL_AUTH_IS_REQUIRED);
  }

  public static ReactAdminError invalidUUIDError() {
    return new ReactAdminError(INVALID_UUID);
  }

  public static class Postgres {
    public static final String POSTGRES_17 = "postgres:17";
    public static final String DB_NAME = "testdb";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
  }

  public static class SpringDataSource {
    public static final String URL = "spring.datasource.url";
    public static final String USERNAME = "spring.datasource.username";
    public static final String PASSWORD = "spring.datasource.password";
  }
}
