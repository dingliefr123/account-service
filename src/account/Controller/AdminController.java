package account.Controller;

import account.DTO.PutRoleDTO;
import account.DTO.SingleUserWithRoleResponse;
import account.Exception.BadRequestException;
import account.Exception.CustomForbiddenException;
import account.Exception.UnauthorizedException;
import account.Service.UserRoleService;
import account.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/")
@Validated
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

  @GetMapping("user")
  List<SingleUserWithRoleResponse> getUsersInfo() {
    return userRoleService.queryUsersWithRoleInfo();
  }

  private static Map<String, String> GetUserDeleteSuccessRes(String user) {
    return Map.of(
            "user", user,
            "status", "Deleted successfully!"
    );
  }

}
