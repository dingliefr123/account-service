package account.Security;

import account.Exception.BadRequestException;
import account.Exception.UserNotFoundException;

public enum Role {
  USER,
  ACCOUNTANT,
  ADMINISTRATOR,
  AUDITOR;


  public static String Serialize(Role role) {
    if (role == USER)
      return CustomAuthority.ROLE_USER_TXT;
    else if (role == ADMINISTRATOR)
      return CustomAuthority.ROLE_ADMINISTRATOR_TXT;
    else if (role == ACCOUNTANT)
      return CustomAuthority.ROLE_ACCOUNTANT_TXT;
    else if (role == AUDITOR)
      return CustomAuthority.ROLE_AUDITOR_TXT;
    return "";
  }

  public static Role fromString(String str) {
    for(var role : Role.values()) {
      if (role.name().equals(str)) return role;
    }
    throw new UserNotFoundException("Role not found!");
  }
}
