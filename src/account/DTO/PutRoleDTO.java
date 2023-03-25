package account.DTO;


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
    REMOVE;

    public boolean IS_GRANT() {
      return this.equals(GRANT);
    }
    public boolean IS_REMOVE() {
      return this.equals(REMOVE);
    }
  }

  public RoleOperation getRoleOperation() {
    if (Objects.equals(operation, RoleOperation.GRANT.name()))
      return RoleOperation.GRANT;
    else
      return RoleOperation.REMOVE;
  }

}
