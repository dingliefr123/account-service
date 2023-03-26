package account.DTO;

import account.Exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutLockDTO {

  @NotNull
  @NotBlank
  private String user;

  @NotNull
  @Pattern(regexp = "(LOCK|UNLOCK)")
  private String operation;

  public PutRoleDTO.RoleOperation getRoleOperation() {
//    for(PutRoleDTO.RoleOperation role : PutRoleDTO.RoleOperation.values()) {
//      if (role.name().equals(operation))
//        return role;
//    }
    return PutRoleDTO.RoleOperation.valueOf(operation);
  }

}
