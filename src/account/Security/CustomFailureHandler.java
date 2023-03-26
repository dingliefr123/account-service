package account.Security;

import account.Service.EventService;
import account.Service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log
public class CustomFailureHandler
        extends SimpleUrlAuthenticationFailureHandler {
  @Autowired
  private UserService userService;

  @Override
  public void onAuthenticationFailure(
          HttpServletRequest request,
          HttpServletResponse response,
          AuthenticationException exception
  ) throws IOException, ServletException {
    CustomUserDetails userDetails =
            (CustomUserDetails) request.getUserPrincipal();

//    if (!userDetails.isAccountNonLocked()) {
//      String userEmail = userDetails.getUsername();
//      boolean isBrutal = userDetails.isBrutal();
//    }

    super.onAuthenticationFailure(request, response, exception);
  }
}
