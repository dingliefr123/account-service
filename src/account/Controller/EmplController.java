package account.Controller;

import account.DTO.SignUpDTO;
import account.DTO.SignUpResponse;
import account.Exception.BadRequestException;
import account.Security.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    SignUpResponse withId = checkAuthenticatedAndGetId();
    logger.info("/payment close id:" + withId.getId());
    return withId;
  }

  static SignUpResponse checkAuthenticatedAndGetId() {
    try {
      Authentication auth =
              SecurityContextHolder.getContext().getAuthentication();
      CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
      var dtoWithId = userDetails.getDtoWithId();
      if (Objects.isNull(dtoWithId.getId()))
        throw new Exception();
      return dtoWithId;
    } catch(Exception e) {
      throw new BadRequestException("Unable to find user info");
    }
  }

}
