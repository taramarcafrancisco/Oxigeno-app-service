package com.oxigeno.portal.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allowed-origin-patterns:http://localhost:3000,http://localhost:5173,https://startb.com.ar,https://www.startb.com.ar,https://webservice.startb.com.ar,https://oxigeno-app-theta.vercel.app,https://*.vercel.app,https://*.netlify.app,https://*.pages.dev,https://*.dev}")
    private String allowedOriginPatterns;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns(Arrays.stream(allowedOriginPatterns.split(","))
                                .map(String::trim)
                                .filter(pattern -> !pattern.isEmpty())
                                .toArray(String[]::new))
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
