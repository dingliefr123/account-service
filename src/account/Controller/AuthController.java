package account.Controller;

import account.DTO.SignUpDTO;
import account.DTO.SignUpResponse;
import account.DTO.UpdatePasswordDTO;
import account.DTO.UpdatePasswordResponse;
import account.Service.UserService;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
@Log
public class AuthController {

  @Autowired
  private UserService userService;

  @PostMapping("signup")
  @ResponseStatus(HttpStatus.OK)
  SignUpResponse signup(@Valid @RequestBody SignUpDTO signUpDTO) {
    // log.info("signup starts");
    SignUpResponse signUpResponse = userService.saveUser(signUpDTO);
    // log.info("signup closes id: " + signUpResponse.getId());
    return signUpResponse;
  }

  @PostMapping("changepass")
  UpdatePasswordResponse changePass(@Valid @RequestBody UpdatePasswordDTO updateDTO) {
    // log.info("changePass starts");
    SignUpResponse signUpResponse =
            userService.changePassword(updateDTO.getNew_password());
    // log.info("changePass closes");
    return new UpdatePasswordResponse(signUpResponse.getEmail().toLowerCase());
  }

}
