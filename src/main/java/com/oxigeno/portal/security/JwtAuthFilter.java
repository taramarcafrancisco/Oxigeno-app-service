package com.oxigeno.portal.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final DbUserDetailsService userDetailsService;

  public JwtAuthFilter(JwtUtil jwtUtil, DbUserDetailsService userDetailsService) {
    this.jwtUtil = jwtUtil;
    this.userDetailsService = userDetailsService;
  }

@Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain chain)
    throws ServletException, IOException {

  // 🔐 ENVOLVER REQUEST
  ContentCachingRequestWrapper wrappedRequest =
      new ContentCachingRequestWrapper(request);

  String authHeader = wrappedRequest.getHeader("Authorization");

  if (authHeader != null && authHeader.startsWith("Bearer ")) {
    String token = authHeader.substring(7);

    if (jwtUtil.esTokenValido(token)
        && SecurityContextHolder.getContext().getAuthentication() == null) {

      String email = jwtUtil.extraerEmail(token);

      UserDetails userDetails =
          userDetailsService.loadUserByUsername(email);

      UsernamePasswordAuthenticationToken authentication =
          new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );

      authentication.setDetails(
          new WebAuthenticationDetailsSource()
              .buildDetails(wrappedRequest)
      );

      SecurityContextHolder.getContext()
          .setAuthentication(authentication);
    }
  }

  // 🚨 PASAR EL WRAPPER, NO EL REQUEST ORIGINAL
  chain.doFilter(wrappedRequest, response);
}}
