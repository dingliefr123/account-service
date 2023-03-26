package account.Util;

import account.DTO.SignUpResponse;
import account.Exception.BadRequestException;
import account.Security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Set;

public class AuthUtil {
  private static final Set<String> BREACHED_PASSWORD_SET = Set.of(
          "PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
          "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
          "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember"
  );

  public static void checkBreachedPwd(String password) {
    if (BREACHED_PASSWORD_SET.contains(password))
      throw new BadRequestException("The password is in the hacker's database!");
  }

  public static SignUpResponse checkAuthAndGetUserInfo() {
    return getUserDetails().getDtoWithId();
  }

  public static void checkCurAndNewPwdNotSame(String newPassword, PasswordEncoder encoder) {
    if(encoder.matches(newPassword, getUserDetails().getPassword()))
      throw new BadRequestException("The passwords must be different!");
  }

  public static String GetCurrentAuthName() {
    return getUserDetails().getUsername();
  }

  private static CustomUserDetails getUserDetails() {
      Authentication auth =
              SecurityContextHolder.getContext().getAuthentication();
      CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
      var dtoWithId = userDetails.getDtoWithId();
      return userDetails;
  }

  public static String GetCurrentUrl() {
    String url = ((ServletRequestAttributes) RequestContextHolder
            .currentRequestAttributes()).getRequest().getRequestURI();
    int idx = url.indexOf("/api");
    if (idx < 0) return "";
    return url.substring(idx);
  }
}
