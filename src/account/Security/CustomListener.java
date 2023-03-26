package account.Security;

import account.Service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Log
public class CustomListener {
  @Autowired
  UserService userService;

  @EventListener
  public void onSuccess(AuthenticationSuccessEvent success) {
    CustomUserDetails userDetail =
            (CustomUserDetails) success.getAuthentication().getPrincipal();
    int wrongInputCnt = userDetail.getDtoWithId().getWrongInputCnt();
    if (wrongInputCnt > 0) {
      String userEmail = userDetail.getUsername();
      userService.unlockAndClearWrongCnt(userEmail);
    }
  }

  @EventListener
  public void onFailure(AbstractAuthenticationFailureEvent failures) {
    String authUserName =
            failures.getAuthentication().getPrincipal().toString();
    userService.updateWrongCntByEmailAndSaveEvent(authUserName);
  }
}
