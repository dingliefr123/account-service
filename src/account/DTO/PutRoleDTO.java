package account.DTO;


import account.Exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutRoleDTO {

  @NotNull
  @NotBlank
  private String user;

  @NotNull
  // @Pattern(regexp = "(USER|ACCOUTANT|ADMINISTRATOR)")
  private String role;

  @NotNull
  @Pattern(regexp = "(GRANT|REMOVE)")
  private String operation;

  public enum RoleOperation {
    GRANT,
    REMOVE,
    LOCK,
    UNLOCK;

    public boolean IS_GRANT() {
      return this.equals(GRANT);
    }
    public boolean IS_REMOVE() {
      return this.equals(REMOVE);
    }
    public boolean IS_LOCK() { return this.equals(LOCK); }
    public boolean IS_UNLOCK () { return this.equals(UNLOCK); }
  }

  public RoleOperation getRoleOperation() {
    for(RoleOperation role : RoleOperation.values()) {
      if (role.name().equals(operation))
        return role;
    }
    throw new BadRequestException("");
  }

}
