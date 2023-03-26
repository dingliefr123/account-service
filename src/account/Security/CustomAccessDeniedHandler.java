package account.Security;

import account.Service.EventService;
import account.Util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Autowired
  EventService eventService;

  @Override
  public void handle(HttpServletRequest request,
                     HttpServletResponse response,
                     AccessDeniedException exc) {
    String subject = AuthUtil.GetCurrentAuthName();
    // @TODO ACCESS_DENIED
    eventService.addAccessDeniedEvent(subject);
    try {
      response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
