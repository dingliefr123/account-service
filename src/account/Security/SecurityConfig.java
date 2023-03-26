package account.Security;

import account.DTO.StandardError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  CustomUserDetailsService detailsService;

  @Autowired
  SecurityConfig(
          CustomUserDetailsService detailsService) {
    this.detailsService = detailsService;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .mvcMatchers(
                    HttpMethod.POST,
                    "/actuator/shutdown", // For testing purposes
                    "/api/auth/signup" // for registering
            ).permitAll()
            .mvcMatchers(HttpMethod.POST,"/pai/auth/changepass")
            .hasAnyAuthority(
                    CustomAuthority.ROLE_ACCOUNTANT_TXT,
                    CustomAuthority.ROLE_USER_TXT,
                    CustomAuthority.ROLE_ADMINISTRATOR_TXT
            ) // all roles allowed to touch
            .mvcMatchers(HttpMethod.GET, "/api/empl/payment")
            .hasAnyAuthority(
                    CustomAuthority.ROLE_USER_TXT,
                    CustomAuthority.ROLE_ACCOUNTANT_TXT
            )
            .mvcMatchers(HttpMethod.POST, "/api/acct/payments")
            .hasAuthority(CustomAuthority.ROLE_ACCOUNTANT_TXT)
            .mvcMatchers("/api/admin/**")
            .hasRole(Role.ADMINISTRATOR.name())
            .mvcMatchers(HttpMethod.GET, "/api/security/events")
            .hasRole(Role.AUDITOR.name())
            .anyRequest().authenticated()
            .and()
//            .formLogin()
//            .failureHandler(authenticationFailureHandler())
//            .and()
            .csrf().disable()
            .headers().frameOptions().disable() // for Postman, the H2 console
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
            .and()
            .httpBasic()
            .authenticationEntryPoint(SecurityConfig::authenticationEntryPoint); // enables basic auth.
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .userDetailsService(detailsService)
      .passwordEncoder(getEncoder());
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler();
  }

  @Bean
  public PasswordEncoder getEncoder() {
    return new BCryptPasswordEncoder(13);
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }

  private static void authenticationEntryPoint(
          HttpServletRequest request,
          HttpServletResponse response,
          AuthenticationException exception
  ) throws IOException, ServletException {
    final ObjectMapper objectMapper = new ObjectMapper();
    final HttpStatus status = HttpStatus.UNAUTHORIZED;
    response.setStatus(status.value());
    String message = exception.getMessage();
    message = StringUtils.isEmpty(message) ? "User account is locked" : message;
    StandardError s = new StandardError(status, message, "Unauthorized");

    response.getOutputStream()
            .println(objectMapper.writeValueAsString(s));
  }
}