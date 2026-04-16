package com.oxigeno.portal.security;

import java.util.Arrays;
<<<<<<< HEAD
import java.util.List;
import java.util.stream.Collectors;
=======
>>>>>>> b7f8d9a (Fix CORS credentials)

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
<<<<<<< HEAD
=======
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
>>>>>>> b7f8d9a (Fix CORS credentials)


@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origin-patterns:http://localhost:5173,https://oxigeno-app-theta.vercel.app}")
    private String allowedOriginPatterns;

    @Bean
<<<<<<< HEAD
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(parseAllowedOriginPatterns());
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
=======
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://oxigeno-app-theta.vercel.app")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(false);
            }
        };
>>>>>>> b7f8d9a (Fix CORS credentials)
    }

    private List<String> parseAllowedOriginPatterns() {
        return Arrays.stream(allowedOriginPatterns.split(","))
                .map(String::trim)
                .filter(pattern -> !pattern.isEmpty())
                .collect(Collectors.toList());
    }
}
