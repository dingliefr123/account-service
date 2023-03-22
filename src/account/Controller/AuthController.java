package account.Controller;

import account.DTO.SignUpDTO;
import account.DTO.SignUpResponse;
import account.DTO.UpdatePasswordDTO;
import account.DTO.UpdatePasswordResponse;
import account.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private UserService userService;

  @PostMapping("signup")
  @ResponseStatus(HttpStatus.OK)
  SignUpResponse signup(@Valid @RequestBody SignUpDTO signUpDTO) {
    logger.info("signup starts");
    SignUpResponse signUpResponse = userService.saveUser(signUpDTO);
    logger.info("signup closes id: " + signUpResponse.getId());
    return signUpResponse;
  }

  @PostMapping("changepass")
  UpdatePasswordResponse changePass(@Valid @RequestBody UpdatePasswordDTO updateDTO) {
    logger.info("changePass starts");
    SignUpResponse signUpResponse =
            userService.changePassword(updateDTO.getNew_password());
    logger.info("changePass closes");
    return new UpdatePasswordResponse(signUpResponse.getEmail().toLowerCase());
  }

}
