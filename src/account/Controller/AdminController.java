package account.Controller;

import account.DTO.PutLockDTO;
import account.DTO.PutRoleDTO;
import account.DTO.SingleUserWithRoleResponse;
import account.Exception.BadRequestException;
import account.Exception.CustomForbiddenException;
import account.Exception.UnauthorizedException;
import account.Service.UserRoleService;
import account.Service.UserService;
import account.Util.AuthUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/")
@Validated
@Log
public class AdminController {
  @Autowired
  UserRoleService userRoleService;

  @Autowired
  UserService userService;

  @PutMapping("user/role")
  SingleUserWithRoleResponse putRole(@Valid @RequestBody PutRoleDTO putRoleDTO) {
    return userRoleService.putRole(putRoleDTO);
  }

  @DeleteMapping("user")
  Map<String, String> errorRoute() {
    throw new CustomForbiddenException("Access Denied!", "Forbidden");
  }

  @DeleteMapping("user/{email}")
  Map<String, String> deleteUser(@PathVariable @NotNull @NotBlank String email) {
    userService.deleteUser(email);
    return GetUserDeleteSuccessRes(email);
  }

  @PutMapping("user/access")
  Map<String, String> putUserLock(@Valid @RequestBody PutLockDTO putAccessDTO) {
    var operation
            = putAccessDTO.getRoleOperation();
    String targetEmail = putAccessDTO.getUser();
    if (operation.IS_LOCK())
      userService.lockUser(AuthUtil.GetCurrentAuthName(), targetEmail);
    else
      userService.unlockAndClearWrongCntAndSaveEvent(targetEmail);
    return GetPutAccessResponse(putAccessDTO, operation);
  }

  @GetMapping("user")
  List<SingleUserWithRoleResponse> getUsersInfo() {
    return userRoleService.queryUsersWithRoleInfo();
  }

  /* Internal Functions */
  private static Map<String, String> GetUserDeleteSuccessRes(String user) {
    return Map.of(
            "user", user.toLowerCase(Locale.ROOT),
            "status", "Deleted successfully!"
    );
  }

  private static Map<String, String> GetPutAccessResponse(
          PutLockDTO dto,
          PutRoleDTO.RoleOperation operation) {
    return Map.of(
            "status",
            String.format("User %s %s!",
                    dto.getUser().toLowerCase(),
                    operation.name().toLowerCase() + "ed")
    );
  }
}
