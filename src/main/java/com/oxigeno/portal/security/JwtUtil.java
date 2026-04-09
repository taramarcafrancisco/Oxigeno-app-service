package com.oxigeno.portal.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration-ms:86400000}")
  private long expirationMs;

  public String generarToken(Integer userId, String email) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
      .setSubject(email)
      .claim("userId", userId)
      .setIssuedAt(now)
      .setExpiration(exp)
      .signWith(SignatureAlgorithm.HS256, secret)
      .compact();
  }

  public String extraerEmail(String token) {
    return getClaims(token).getSubject();
  }

  public boolean esTokenValido(String token) {
    try {
      Claims claims = getClaims(token);
      return claims.getExpiration().after(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
      .setSigningKey(secret)
      .parseClaimsJws(token)
      .getBody();
  }
}
