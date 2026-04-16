package com.oxigeno.portal.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;
  private final List<String> allowedOriginPatterns;

  public SecurityConfig(
      JwtAuthFilter jwtAuthFilter,
      @Value("${app.cors.allowed-origin-patterns:http://localhost:3000,http://localhost:5173,https://startb.com.ar,https://www.startb.com.ar,https://webservice.startb.com.ar,https://oxigeno-app-theta.vercel.app,https://*.vercel.app,https://*.netlify.app,https://*.pages.dev,https://*.dev}") String allowedOriginPatterns
  ) {
    this.jwtAuthFilter = jwtAuthFilter;
    this.allowedOriginPatterns = Arrays.stream(allowedOriginPatterns.split(","))
        .map(String::trim)
        .filter(pattern -> !pattern.isEmpty())
        .collect(Collectors.toList());
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .cors(Customizer.withDefaults())
      .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth -> auth
        .antMatchers("/api/auth/**").permitAll()
        .antMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
        .anyRequest().authenticated()
      );

    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {

      CorsConfiguration configuration = new CorsConfiguration();

      configuration.setAllowedOriginPatterns(allowedOriginPatterns);

      configuration.setAllowedMethods(Arrays.asList(
          "GET",
          "POST",
          "PUT",
          "DELETE",
          "PATCH",
          "OPTIONS"
      ));

      configuration.setAllowedHeaders(Arrays.asList("*"));
      configuration.setExposedHeaders(Arrays.asList("Authorization"));

      configuration.setAllowCredentials(true);

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);

      return source;
  }
}
