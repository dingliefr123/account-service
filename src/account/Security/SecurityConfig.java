package account.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  CustomUserDetailsService detailsService;

  @Autowired
  SecurityConfig(CustomUserDetailsService detailsService) {
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
            .anyRequest().authenticated()
            .and()
            .csrf().disable()
            .headers().frameOptions().disable() // for Postman, the H2 console
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // no session
            .and()
            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
            .and()
            .httpBasic(); // enables basic auth.
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .userDetailsService(detailsService)
      .passwordEncoder(getEncoder());
  }

  @Bean
  public PasswordEncoder getEncoder() {
    return new BCryptPasswordEncoder(13);
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    return new CustomAccessDeniedHandler();
  }
}