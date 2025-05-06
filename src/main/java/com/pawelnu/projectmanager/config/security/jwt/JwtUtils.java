package com.pawelnu.projectmanager.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtUtils {

  @Value("${spring.app.jwtSecret}")
  private String jwtSecret;

  @Value("${spring.app.jwtExpirationMin}")
  private int jwtExpirationMin;

  public String getJwtFromHeader(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    log.debug("Authorization Header: {}", bearerToken);
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7); // Remove Bearer prefix
    }
    return null;
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder()
        .subject(username)
        .issuedAt(new Date())
        .expiration(new Date((new Date()).getTime() + (jwtExpirationMin * 60 * 1000)))
        .signWith(key())
        .compact();
  }

  public String getUserNameFromJwtHeaderToken(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) key())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public Date getExpirationDateFromToken(String token) {
    Claims claims = getClaimsFromToken(token);
    return claims.getExpiration();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  //  public boolean validateJwtToken(String authToken) {
  //    try {
  //      Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
  //      return true;
  //    } catch (MalformedJwtException e) {
  //      log.error("Invalid JWT token: {}", e.getMessage());
  //    } catch (ExpiredJwtException e) {
  //      log.error("JWT token is expired: {}", e.getMessage());
  //    } catch (UnsupportedJwtException e) {
  //      log.error("JWT token is unsupported: {}", e.getMessage());
  //    } catch (IllegalArgumentException e) {
  //      log.error("JWT claims string is empty: {}", e.getMessage());
  //    }
  //    return false;
  //  }

  private Claims getClaimsFromToken(String authToken) {
    try {
      return Jwts.parser()
          .verifyWith((SecretKey) key())
          .build()
          .parseSignedClaims(authToken)
          .getPayload();
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return null;
  }

  public boolean validateJwtToken(String authToken) {
    try {
      getClaimsFromToken(authToken);
      return true;
    } catch (Exception e) {
      log.error("JWT token validation failed: {}", e.getMessage());
    }
    return false;
  }
}
