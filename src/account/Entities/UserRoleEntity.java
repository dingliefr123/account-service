package account.Entities;

import account.Security.Role;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_role")
public class UserRoleEntity {

  @Id
  @GeneratedValue
  @Column(name = "user_role_id")
  private Long id;

  @Enumerated(EnumType.STRING)
  private Role role;


  public static UserRoleEntity GetUserRole() {
    return UserRoleEntity.builder().role(Role.USER).build();
  }

  public static UserRoleEntity GetAdminRole() {
    return UserRoleEntity.builder().role(Role.ADMINISTRATOR).build();
  }

  public static UserRoleEntity GetAcctRole() {
    return UserRoleEntity.builder().role(Role.ACCOUNTANT).build();
  }

  public static UserRoleEntity GETAuditorRole () {
    return UserRoleEntity.builder().role(Role.AUDITOR).build();
  }

  public static UserRoleEntity FromRole(Role role) {
    if (role.equals(Role.USER))
      return GetUserRole();
    else if (role.equals(Role.AUDITOR))
      return GETAuditorRole();
    else
      return GetAcctRole();
  }
}
