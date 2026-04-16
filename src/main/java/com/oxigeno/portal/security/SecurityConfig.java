package com.oxigeno.portal.security;

<<<<<<< HEAD
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
=======
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
>>>>>>> b7f8d9a (Fix CORS credentials)
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
<<<<<<< HEAD
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
=======
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
>>>>>>> b7f8d9a (Fix CORS credentials)

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors()
            .and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .antMatchers("/api/auth/**").permitAll()
            .anyRequest().authenticated();

<<<<<<< HEAD
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
=======
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }
>>>>>>> b7f8d9a (Fix CORS credentials)
}
