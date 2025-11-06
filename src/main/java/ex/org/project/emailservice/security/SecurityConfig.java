package ex.org.project.emailservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for Email Service.
 * 
 * Provides two security filter chains:
 * 1. HTTP Basic Auth for actuator endpoints (shutdown, health)
 * 2. Permit all for API endpoints (Lambda function invocations)
 */
@Configuration
public class SecurityConfig {

  /**
   * Security filter chain for actuator endpoints using HTTP Basic Auth.
   * This allows management operations (like shutdown) to use simple username/password.
   * 
   * @param http HttpSecurity configuration
   * @return Configured SecurityFilterChain for actuator endpoints
   * @throws Exception if configuration fails
   */
  @Bean
  @Order(1) // Higher priority than the API filter chain
  public SecurityFilterChain actuatorSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/email/v1/actuator/**")  // Match actuator path explicitly
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/email/v1/actuator/health").permitAll()
            .requestMatchers("/api/email/v1/actuator/shutdown").authenticated()
            .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf
            .ignoringRequestMatchers("/api/email/v1/actuator/shutdown")
        )
        .httpBasic(httpBasic -> {}); // Enable HTTP Basic Auth for actuator endpoints

    return http.build();
  }

  /**
   * Security filter chain for API endpoints - all public.
   * Email service uses SQS Lambda triggers, not HTTP authentication.
   * 
   * @param http HttpSecurity configuration
   * @return Configured SecurityFilterChain for API endpoints
   * @throws Exception if configuration fails
   */
  @Bean
  @Order(2) // Lower priority - handles all non-actuator requests
  public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        // Explicitly match only non-actuator paths
        .securityMatcher(request -> !request.getRequestURI().startsWith("/api/email/v1/actuator"))
        
        // Disable CSRF - not needed for Lambda/SQS triggers
        .csrf(csrf -> csrf.disable())
        
        // All API endpoints are public (Lambda invocations)
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll()
        );

    return http.build();
  }
}

