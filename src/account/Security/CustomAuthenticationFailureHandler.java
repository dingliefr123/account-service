package account.Security;

import account.DTO.StandardError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void onAuthenticationFailure(
          HttpServletRequest req,
          HttpServletResponse response,
          AuthenticationException ex) throws IOException {
    final HttpStatus status = HttpStatus.UNAUTHORIZED;
    response.setStatus(status.value());
    StandardError s = new StandardError(status, ex.getMessage(), status.name());

    response.getOutputStream()
            .println(objectMapper.writeValueAsString(s));
  }

}