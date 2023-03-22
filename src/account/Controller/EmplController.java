package account.Controller;

import account.DTO.SignUpResponse;
import account.Util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/empl")
public class EmplController {

  private static final Logger logger = LoggerFactory.getLogger(EmplController.class);

  @GetMapping("/payment")
  SignUpResponse getPayment() {
    logger.info("/payment entry");
    SignUpResponse withId = AuthUtil.checkAuthAndGetUserInfo();
    logger.info("/payment close id:" + withId.getId());
    return withId;
  }

}
